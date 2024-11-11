from concurrent.futures import ThreadPoolExecutor
import asyncio
from functools import partial
from fastapi import FastAPI, HTTPException, File, UploadFile
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import RedirectResponse
from pydantic import BaseModel, Field
from typing import List, Dict, TypeVar, Generic, Optional
from contextlib import asynccontextmanager
import face_recognition
import numpy as np
import requests
from io import BytesIO
from PIL import Image, ImageEnhance, ExifTags
import cv2
import logging
import os
from dotenv import load_dotenv
import socket
import contextlib
from py_eureka_client import eureka_client

# .env 파일 로드
load_dotenv()

# 환경변수 설정
EUREKA_SERVER = os.getenv('EUREKA_SERVER')
APP_NAME = os.getenv('APP_NAME')
SERVER_HOST = os.getenv('SERVER_HOST','0.0.0.0')
INSTANCE_HOST = os.getenv('INSTANCE_HOST')
SERVER_PORT = int(os.getenv('SERVER_PORT'))

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Startup
    try:
        logger.info(f"Using port: {SERVER_PORT}")
        # Eureka 클라이언트 초기화
        eureka_config = {
            "eureka_server": EUREKA_SERVER,
            "app_name": APP_NAME,
            "instance_port": SERVER_PORT,
            "instance_host": INSTANCE_HOST
        }
        logger.info(f"Eureka configuration: {eureka_config}")
        
        # 필수 값 검증
        if not all([EUREKA_SERVER, APP_NAME, INSTANCE_HOST, SERVER_PORT]):
            missing_values = []
            if not EUREKA_SERVER: missing_values.append("EUREKA_SERVER")
            if not APP_NAME: missing_values.append("APP_NAME")
            if not INSTANCE_HOST: missing_values.append("INSTANCE_HOST")
            if not SERVER_PORT: missing_values.append("SERVER_PORT")
            raise ValueError(f"Missing required configuration: {', '.join(missing_values)}")

        await eureka_client.init_async(
            eureka_server=EUREKA_SERVER,
            app_name=APP_NAME,
            instance_port=SERVER_PORT,
            instance_host=INSTANCE_HOST,
            instance_ip=INSTANCE_HOST
        )
        logger.info(f"Successfully registered to Eureka server at {EUREKA_SERVER}")
    except Exception as e:
        logger.error(f"Failed to register to Eureka server: {str(e)}")
        raise
    
    yield
    
    # Shutdown
    try:
        await eureka_client.stop_async()
        logger.info("Successfully unregistered from Eureka server")
    except Exception as e:
        logger.error(f"Error during Eureka client shutdown: {str(e)}")

app = FastAPI(
    title="Face Classification API",
    description="얼굴 유사도 분석 API",
    version="1.0.0",
    lifespan=lifespan,
    openapi_url="/face-recognition/openapi.json",  
    docs_url="/face-recognition/docs",          
    redoc_url=None
)

# CORS 미들웨어 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

T = TypeVar('T')

class BaseResponse(BaseModel, Generic[T]):
    statusCode: int
    message: str
    data: Optional[T] = None

    class Config:
        arbitrary_types_allowed = True

    @classmethod
    def create(cls, status_code: int, message: str, data: Optional[T] = None):
        return cls(
            statusCode=status_code,
            message=message,
            data=data
        )

class Person(BaseModel):
    id: int
    photoUrl: str = Field(..., alias="photoUrl")

class AnalysisRequest(BaseModel):
    targetImages: List[str] = Field(..., alias="targetImages")
    people: List[Person]

class SimilarityResponse(BaseModel):
    imageUrl: str = Field(..., alias="imageUrl")
    similarities: Dict[int, float]
    faceCount: int = Field(..., alias="faceCount")

class CountResponse(BaseModel):
    faceCount: int = Field(..., alias="faceCount")

async def load_people_encodings(people):
    """여러 인물의 얼굴 인코딩을 병렬로 처리"""
    def load_single_person(person):
        img = load_image_from_url(person.photoUrl)
        encodings = get_face_encodings(img)
        return person.id, encodings[0] if encodings and len(encodings) > 0 else None

    tasks = []
    with ThreadPoolExecutor(max_workers=4) as executor:
        loop = asyncio.get_event_loop()
        for person in people:
            task = loop.run_in_executor(executor, load_single_person, person)
            tasks.append(task)
        
        results = await asyncio.gather(*tasks)
        return {person_id: encoding for person_id, encoding in results if encoding is not None}

def fix_image_rotation(image):
    """
    EXIF 정보를 기반으로 이미지 회전을 보정합니다.
    """
    try:
        # PIL Image가 아닌 경우 변환
        if not isinstance(image, Image.Image):
            image = Image.fromarray(image)

        try:
            # EXIF 정보 가져오기
            for orientation in ExifTags.TAGS.keys():
                if ExifTags.TAGS[orientation] == 'Orientation':
                    break
            
            exif = image._getexif()
            if exif is not None:
                orientation_value = exif.get(orientation)
                
                # 방향에 따른 이미지 회전
                if orientation_value == 2:
                    image = image.transpose(Image.FLIP_LEFT_RIGHT)
                elif orientation_value == 3:
                    image = image.transpose(Image.ROTATE_180)
                elif orientation_value == 4:
                    image = image.transpose(Image.FLIP_TOP_BOTTOM)
                elif orientation_value == 5:
                    image = image.transpose(Image.FLIP_LEFT_RIGHT).transpose(Image.ROTATE_90)
                elif orientation_value == 6:
                    image = image.transpose(Image.ROTATE_270)
                elif orientation_value == 7:
                    image = image.transpose(Image.FLIP_LEFT_RIGHT).transpose(Image.ROTATE_270)
                elif orientation_value == 8:
                    image = image.transpose(Image.ROTATE_90)
        except (AttributeError, KeyError, IndexError):
            # EXIF 정보가 없거나 처리할 수 없는 경우 원본 이미지 반환
            pass

        # numpy 배열로 변환하여 반환
        return np.array(image)
    except Exception as e:
        logger.error(f"이미지 회전 보정 실패: {str(e)}")
        return np.array(image)

async def process_target_image(target_url, people_encodings):
    """단일 타겟 이미지 처리"""
    def process_single_image(url, encodings):
        target_img = load_image_from_url(url)
        target_encodings = get_face_encodings(target_img)
        
        face_count = len(target_encodings) if target_encodings else 0
        max_similarities = {person_id: 0.0 for person_id in encodings.keys()}
        
        if target_encodings:
            for target_encoding in target_encodings:
                for person_id, person_encoding in encodings.items():
                    if person_encoding is not None:
                        similarity = face_recognition.face_distance([person_encoding], target_encoding)[0]
                        similarity = max(0, 1 - similarity)
                        max_similarities[person_id] = max(
                            max_similarities[person_id],
                            float(similarity)
                        )
        
        return SimilarityResponse(
            imageUrl=url,
            similarities=max_similarities,
            faceCount=face_count
        )
    
    loop = asyncio.get_event_loop()
    with ThreadPoolExecutor() as executor:
        result = await loop.run_in_executor(
            executor,
            process_single_image,
            target_url,
            people_encodings
        )
    return result
    
    return SimilarityResponse(
        imageUrl=target_url,
        similarities=max_similarities,
        faceCount=face_count
    )

def preprocess_image(image):
    """
    이미지 전처리 함수 (EXIF 회전 보정 추가)
    """
    try:
        # PIL Image를 RGB로 변환
        if image.mode != 'RGB':
            image = image.convert('RGB')
        
        # EXIF 기반 회전 보정
        rotated_image = fix_image_rotation(image)
        
        # 히스토그램 평준화
        img_lab = cv2.cvtColor(rotated_image, cv2.COLOR_RGB2LAB)
        l, a, b = cv2.split(img_lab)
        clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8,8))
        cl = clahe.apply(l)
        img_lab = cv2.merge((cl,a,b))
        img_rgb = cv2.cvtColor(img_lab, cv2.COLOR_LAB2RGB)
        
        return img_rgb
        
    except Exception as e:
        logger.error(f"이미지 전처리 실패: {str(e)}")
        return np.array(image)

def load_image_from_url(url: str):
    try:
        response = requests.get(url)
        response.raise_for_status()
        img = Image.open(BytesIO(response.content))
        
        # 이미지 전처리 적용 (EXIF 회전 보정 포함)
        processed_img = preprocess_image(img)
        
        return processed_img
    except Exception as e:
        logger.error(f"이미지 로드 실패: {url} - {str(e)}")
        return None

def load_image_from_bytes(file_content: bytes):
    try:
        img = Image.open(BytesIO(file_content))
        processed_img = preprocess_image(img)
        return processed_img
    except Exception as e:
        logger.error(f"이미지 로드 실패: {str(e)}")
        return None

def get_face_encodings(image):
    if image is None:
        logger.error("입력 이미지가 None입니다")
        return None
    
    try:
        # 이미지 크기 조정
        height, width = image.shape[:2]
        max_dimension = 1500  # 1500에서 2000으로 증가
        logger.info(f"원본 이미지 크기: {width}x{height}")
        
        if max(height, width) > max_dimension:
            scale = max_dimension / max(height, width)
            image = cv2.resize(image, None, fx=scale, fy=scale)
            new_height, new_width = image.shape[:2]
            logger.info(f"이미지 크기 조정: {new_width}x{new_height} (scale: {scale:.2f})")

        # HOG로 얼굴 검출
        logger.info("얼굴 검출 시도")
        face_locations = face_recognition.face_locations(
            image,
            model="hog",
            number_of_times_to_upsample=1
        )
        
        if not face_locations:
            logger.warning("얼굴 검출 실패")
            return None
            
        # 검출된 얼굴 위치 로깅
        logger.info(f"검출된 얼굴 수: {len(face_locations)}")
        for i, (top, right, bottom, left) in enumerate(face_locations):
            logger.info(f"얼굴 {i+1} 위치: top={top}, right={right}, bottom={bottom}, left={left}")
            
        # 얼굴 인코딩
        logger.info("얼굴 인코딩 시작")
        face_encodings = face_recognition.face_encodings(
            image,
            face_locations,
            num_jitters=1
        )
    
        if face_encodings:
            logger.info(f"얼굴 인코딩 완료: {len(face_encodings)}개")
            return face_encodings
        else:
            logger.warning("얼굴 인코딩 실패")
            return None
            
    except Exception as e:
        logger.error(f"얼굴 인코딩 중 에러 발생: {str(e)}")
        return None

@app.post("/face-recognition/classification", response_model=BaseResponse[List[SimilarityResponse]])
async def classify_images(request: AnalysisRequest):
    """여러 이미지에서 검출된 얼굴들 중 각 인물별 최대 유사도를 병렬로 분석"""
    try:
        # 1. 병렬로 인물 인코딩 처리
        logger.info("인물 인코딩 시작")
        people_encodings = await load_people_encodings(request.people)
        logger.info(f"인물 인코딩 완료: {len(people_encodings)}명")
        
        # 2. 타겟 이미지 병렬 처리
        logger.info("타겟 이미지 처리 시작")
        tasks = []
        for target_url in request.targetImages:
            task = process_target_image(target_url, people_encodings)
            tasks.append(task)
        
        results = await asyncio.gather(*tasks)
        logger.info(f"타겟 이미지 처리 완료: {len(results)}개")
        
        return BaseResponse.create(
            status_code=200,
            message="얼굴 유사도 분석이 완료되었습니다.",
            data=results
        )
        
    except Exception as e:
        logger.error(f"Classification API 에러: {str(e)}")
        return BaseResponse.create(
            status_code=500,
            message=f"얼굴 유사도 분석 중 오류가 발생했습니다: {str(e)}"
        )

@app.post("/face-recognition/face-count", response_model=BaseResponse[CountResponse])
async def count_faces(file: UploadFile = File(...)):
    try:
        logger.info(f"얼굴 수 검출 요청 시작 - 파일명: {file.filename}")
        
        # 파일 확장자 검사
        allowed_extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"}
        file_ext = file.filename.lower()[file.filename.rfind("."):]
        
        if file_ext not in allowed_extensions:
            return BaseResponse.create(
                status_code=400,
                message="지원되지 않는 파일 형식입니다. JPG, JPEG, PNG, GIF, BMP, WEBP 파일만 허용됩니다."
            )
        
        # 파일 크기 제한 (20MB)
        MAX_FILE_SIZE = 20 * 1024 * 1024 
        file_content = await file.read()
        file_size = len(file_content)
        
        if file_size > MAX_FILE_SIZE:
            return BaseResponse.create(
                status_code=400,
                message="파일 크기가 너무 큽니다. 최대 20MB까지 허용됩니다."
            )

        # 이미지 처리를 별도 스레드에서 실행
        loop = asyncio.get_event_loop()
        with ThreadPoolExecutor() as executor:
            img = await loop.run_in_executor(executor, load_image_from_bytes, file_content)
            if img is None:
                return BaseResponse.create(
                    status_code=400,
                    message="이미지를 처리할 수 없습니다."
                )
            
            face_encodings = await loop.run_in_executor(executor, get_face_encodings, img)
        
        face_count = len(face_encodings) if face_encodings else 0
        
        return BaseResponse.create(
            status_code=200,
            message="얼굴 수 검출이 완료되었습니다.",
            data=CountResponse(faceCount=face_count)
        )
        
    except Exception as e:
        logger.error(f"얼굴 수 검출 중 오류 발생: {str(e)}")
        return BaseResponse.create(
            status_code=500,
            message=f"얼굴 수 검출 중 오류가 발생했습니다: {str(e)}"
        )

async def register_to_eureka():
    """Eureka 서버에 서비스 등록"""
    global server_port
    try:
        # eureka_client 설정
        eureka_config = {
            "eureka_server": EUREKA_SERVER,
            "app_name": APP_NAME,
            "instance_port": SERVER_PORT,
            "instance_host": INSTANCE_HOST
        }
        
        # 설정값 로깅
        logger.info(f"Eureka configuration: {eureka_config}")
        
        # 필수 값 검증
        if not all([EUREKA_SERVER, APP_NAME, INSTANCE_HOST, SERVER_PORT]):
            missing_values = []
            if not EUREKA_SERVER: missing_values.append("EUREKA_SERVER")
            if not APP_NAME: missing_values.append("APP_NAME")
            if not INSTANCE_HOST: missing_values.append("INSTANCE_HOST")
            if not SERVER_PORT: missing_values.append("SERVER_PORT")
            raise ValueError(f"Missing required configuration: {', '.join(missing_values)}")

        # Eureka 클라이언트 초기화
        await eureka_client.init_async(
            eureka_server=EUREKA_SERVER,
            app_name=APP_NAME,
            instance_port=SERVER_PORT,
            instance_host=INSTANCE_HOST,
            instance_ip=INSTANCE_HOST
        )
        logger.info(f"Successfully registered to Eureka server at {EUREKA_SERVER}")
    except Exception as e:
        logger.error(f"Failed to register to Eureka server: {str(e)}")
        raise

if __name__ == "__main__":
    import uvicorn
    logger.info(f"Starting server on port {SERVER_PORT}")
    uvicorn.run(
        app, 
        host=SERVER_HOST,
        port=SERVER_PORT,
    )