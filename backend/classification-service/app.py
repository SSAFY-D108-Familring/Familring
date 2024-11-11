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
from PIL import Image, ImageEnhance
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
    id: str
    photoUrl: str = Field(..., alias="photoUrl")

class AnalysisRequest(BaseModel):
    targetImages: List[str] = Field(..., alias="targetImages")
    people: List[Person]

class SimilarityResponse(BaseModel):
    imageUrl: str = Field(..., alias="imageUrl")
    similarities: Dict[str, float]
    faceCount: int = Field(..., alias="faceCount")

class CountResponse(BaseModel):
    faceCount: int = Field(..., alias="faceCount")

def preprocess_image(image):
    """
    이미지 전처리 함수 (단순화)
    - RGB 형식으로 변환
    - 기본적인 히스토그램 평준화
    """
    try:
        # PIL Image를 RGB로 변환
        if image.mode != 'RGB':
            image = image.convert('RGB')
        
        # numpy 배열로 변환
        img_array = np.array(image)
        
        # 기본적인 히스토그램 평준화
        img_lab = cv2.cvtColor(img_array, cv2.COLOR_RGB2LAB)
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
        
        # 이미지 전처리 적용
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
        max_dimension = 2000  # 1500에서 2000으로 증가
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

# classification
@app.post("/face-recognition/classification", response_model=BaseResponse[List[SimilarityResponse]])
async def classify_images(request: AnalysisRequest):
    """
    여러 이미지에서 검출된 얼굴들 중 각 인물별 최대 유사도를 분석합니다.
    """
    try:
        # 기존 로직 유지
        people_encodings = {}
        for person in request.people:
            img = load_image_from_url(person.photoUrl)
            encodings = get_face_encodings(img)
            if encodings and len(encodings) > 0:
                people_encodings[person.id] = encodings[0]
                logger.info(f"사람 등록 성공: {person.id}")
            else:
                logger.warning(f"얼굴 감지 실패: {person.id} - 유사도 0으로 처리됩니다")
                people_encodings[person.id] = None

        results = []
        
        for target_url in request.targetImages:
            # 기존 로직 유지
            target_img = load_image_from_url(target_url)
            target_encodings = get_face_encodings(target_img)
            
            face_count = len(target_encodings) if target_encodings else 0
            max_similarities = {person_id: 0.0 for person_id in people_encodings.keys()}
            
            if target_encodings:
                for target_encoding in target_encodings:
                    for person_id, person_encoding in people_encodings.items():
                        if person_encoding is not None:
                            similarity = face_recognition.face_distance([person_encoding], target_encoding)[0]
                            similarity = max(0, 1 - similarity)
                            max_similarities[person_id] = max(
                                max_similarities[person_id],
                                float(similarity)
                            )
            
            results.append(SimilarityResponse(
                imageUrl=target_url,
                similarities=max_similarities,
                faceCount=face_count
            ))
        
        return BaseResponse.create(
            status_code=200,
            message="얼굴 유사도 분석이 완료되었습니다.",
            data=results
        )
    except Exception as e:
        logger.error(f"얼굴 유사도 분석 중 오류 발생: {str(e)}")
        return BaseResponse.create(
            status_code=500,
            message=f"얼굴 유사도 분석 중 오류가 발생했습니다: {str(e)}"
        )

# face-count 엔드포인트 수정
@app.post("/face-recognition/face-count", response_model=BaseResponse[CountResponse])
async def count_faces(file: UploadFile = File(...)):
    """
    업로드된 이미지 파일에서 검출된 얼굴의 수를 반환합니다.
    """
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
        
        # 파일 크기 제한 (10MB)
        MAX_FILE_SIZE = 10 * 1024 * 1024
        file_content = await file.read()
        file_size = len(file_content)
        
        if file_size > MAX_FILE_SIZE:
            return BaseResponse.create(
                status_code=400,
                message="파일 크기가 너무 큽니다. 최대 10MB까지 허용됩니다."
            )

        img = load_image_from_bytes(file_content)
        if img is None:
            return BaseResponse.create(
                status_code=400,
                message="이미지를 처리할 수 없습니다."
            )
        
        face_encodings = get_face_encodings(img)
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