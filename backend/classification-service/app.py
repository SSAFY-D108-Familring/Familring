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
import asyncio
import concurrent.futures
import aiohttp
import datetime
from pytz import timezone
import time
from prometheus_fastapi_instrumentator import Instrumentator
import json
import math

# .env 파일 로드
load_dotenv()

# 환경변수 설정
EUREKA_SERVER = os.getenv('EUREKA_SERVER')
APP_NAME = os.getenv('APP_NAME')
SERVER_HOST = os.getenv('SERVER_HOST','0.0.0.0')
INSTANCE_HOST = os.getenv('INSTANCE_HOST')
SERVER_PORT = int(os.getenv('SERVER_PORT'))

# CPU 코어 수에 기반한 스레드 풀 최적화
CPU_COUNT = os.cpu_count() or 4
THREAD_POOL = concurrent.futures.ThreadPoolExecutor(max_workers=CPU_COUNT * 2)

# 로깅 포맷터에 한국 시간대 적용
class KSTFormatter(logging.Formatter):
    def converter(self, timestamp):
        dt = datetime.datetime.fromtimestamp(timestamp)
        korean_tz = timezone('Asia/Seoul')
        return dt.replace(tzinfo=datetime.timezone.utc).astimezone(korean_tz)
        
    def formatTime(self, record, datefmt=None):
        dt = self.converter(record.created)
        if datefmt:
            return dt.strftime(datefmt)
        return dt.strftime('%Y-%m-%d %H:%M:%S')

# 로깅 설정 수정
def setup_logger():
    # 기존 핸들러 제거
    logging.getLogger().handlers.clear()
    
    logger = logging.getLogger(__name__)
    logger.setLevel(logging.INFO)
    
    # 이미 핸들러가 있다면 제거
    if logger.handlers:
        logger.handlers.clear()
    
    # 새로운 핸들러 추가
    formatter = KSTFormatter(
        fmt='%(asctime)s [%(levelname)s] %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S'
    )
    handler = logging.StreamHandler()
    handler.setFormatter(formatter)
    
    logger.addHandler(handler)
    
    # 상위 로거로 전파하지 않도록 설정
    logger.propagate = False
    
    return logger

# 로거 초기화
logger = setup_logger()

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

# 동시성 제어를 위한 상수와 세마포어
MAX_CONCURRENT_REQUESTS = 15
request_semaphore = asyncio.Semaphore(MAX_CONCURRENT_REQUESTS)

async def adaptive_processing(processing_func, *args, **kwargs):
    """요청을 처리하는 래퍼 함수"""
    try:
        async with request_semaphore:  # context manager 사용으로 안전한 release 보장
            result = await processing_func(*args, **kwargs)
            return result
    except Exception as e:
        raise

# Prometheus 설정
instrumentator = Instrumentator()
instrumentator.instrument(app)
instrumentator.expose(app, endpoint="/face-recognition/metrics")

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
    
    def to_json_log(self):
        """응답을 JSON 형식의 문자열로 변환"""
        return json.dumps(
            self.model_dump(exclude_none=True),
            ensure_ascii=False,
            indent=2
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

async def fix_image_rotation(image):
    """
    EXIF 정보를 기반으로 이미지 회전을 보정합니다.
    """
    try:
        loop = asyncio.get_event_loop()
        
        # PIL Image가 아닌 경우 변환
        if not isinstance(image, Image.Image):
            image = await loop.run_in_executor(
                THREAD_POOL,
                lambda: Image.fromarray(image)
            )

        try:
            # EXIF 정보 가져오기
            for orientation in ExifTags.TAGS.keys():
                if ExifTags.TAGS[orientation] == 'Orientation':
                    break
            
            exif = await loop.run_in_executor(
                THREAD_POOL,
                lambda: image._getexif()
            )
            
            if exif is not None:
                orientation_value = exif.get(orientation)
                
                # 방향에 따른 이미지 회전
                if orientation_value == 2:
                    image = await loop.run_in_executor(
                        THREAD_POOL,
                        lambda: image.transpose(Image.FLIP_LEFT_RIGHT)
                    )
                elif orientation_value == 3:
                    image = await loop.run_in_executor(
                        THREAD_POOL,
                        lambda: image.transpose(Image.ROTATE_180)
                    )
                elif orientation_value == 4:
                    image = await loop.run_in_executor(
                        THREAD_POOL,
                        lambda: image.transpose(Image.FLIP_TOP_BOTTOM)
                    )
                elif orientation_value == 5:
                    image = await loop.run_in_executor(
                        THREAD_POOL,
                        lambda: image.transpose(Image.FLIP_LEFT_RIGHT).transpose(Image.ROTATE_90)
                    )
                elif orientation_value == 6:
                    image = await loop.run_in_executor(
                        THREAD_POOL,
                        lambda: image.transpose(Image.ROTATE_270)
                    )
                elif orientation_value == 7:
                    image = await loop.run_in_executor(
                        THREAD_POOL,
                        lambda: image.transpose(Image.FLIP_LEFT_RIGHT).transpose(Image.ROTATE_270)
                    )
                elif orientation_value == 8:
                    image = await loop.run_in_executor(
                        THREAD_POOL,
                        lambda: image.transpose(Image.ROTATE_90)
                    )
        except (AttributeError, KeyError, IndexError):
            # EXIF 정보가 없거나 처리할 수 없는 경우 원본 이미지 반환
            pass

        # numpy 배열로 변환하여 반환
        return await loop.run_in_executor(
            THREAD_POOL,
            lambda: np.array(image)
        )
    except Exception as e:
        logger.error(f"이미지 회전 보정 실패: {str(e)}")
        return await loop.run_in_executor(
            THREAD_POOL,
            lambda: np.array(image)
        )
    
async def preprocess_image(image):
    """
    이미지 전처리 함수 (EXIF 회전 보정)
    """
    try:
        loop = asyncio.get_event_loop()
        
        # numpy array인 경우와 PIL Image인 경우를 구분하여 처리
        if isinstance(image, np.ndarray):
            # 이미 numpy array인 경우
            rotated_image = await fix_image_rotation(image)
        else:
            # PIL Image인 경우
            if image.mode != 'RGB':
                image = await loop.run_in_executor(
                    THREAD_POOL,
                    lambda: image.convert('RGB')
                )
            rotated_image = await fix_image_rotation(image)
        
        # 히스토그램 평준화
        img_lab = await loop.run_in_executor(
            THREAD_POOL,
            lambda: cv2.cvtColor(rotated_image, cv2.COLOR_RGB2LAB)
        )
        
        l, a, b = cv2.split(img_lab)
        clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8,8))
        
        cl = await loop.run_in_executor(
            THREAD_POOL,
            lambda: clahe.apply(l)
        )
        
        img_lab = await loop.run_in_executor(
            THREAD_POOL,
            lambda: cv2.merge((cl,a,b))
        )
        
        img_rgb = await loop.run_in_executor(
            THREAD_POOL,
            lambda: cv2.cvtColor(img_lab, cv2.COLOR_LAB2RGB)
        )
        
        return img_rgb
        
    except Exception as e:
        logger.error(f"이미지 전처리 실패: {str(e)}")
        # 전처리가 실패한 경우 원본 이미지를 numpy array로 반환
        if isinstance(image, np.ndarray):
            return image
        return await loop.run_in_executor(
            THREAD_POOL,
            lambda: np.array(image)
        )

async def load_image_from_url_async(url: str, session: aiohttp.ClientSession):
    """비동기적으로 URL에서 이미지를 로드 - 메모리 관리 개선"""
    try:
        async with session.get(url) as response:
            response.raise_for_status()
            content = await response.read()
            
            loop = asyncio.get_event_loop()
            img = await loop.run_in_executor(
                THREAD_POOL,
                lambda: np.ascontiguousarray(
                    cv2.imdecode(
                        np.frombuffer(content, np.uint8),
                        cv2.IMREAD_COLOR
                    )
                )
            )
            
            if img is None:
                raise ValueError("이미지를 디코딩할 수 없습니다")
                
            processed_img = await preprocess_image(img)
            del img
            return processed_img
            
    except Exception as e:
        logger.error(f"이미지 로드 실패: {url} - {str(e)}")
        return None
    finally:
        if 'content' in locals():
            del content


async def load_image_from_bytes(file_content: bytes):
    """
    바이트 데이터로부터 이미지를 로드하고 전처리합니다.
    """
    try:
        loop = asyncio.get_event_loop()
        img = await loop.run_in_executor(
            THREAD_POOL,
            lambda: Image.open(BytesIO(file_content))
        )
        processed_img = await preprocess_image(img)
        return processed_img
    except Exception as e:
        logger.error(f"이미지 로드 실패: {str(e)}")
        return None

async def process_face_encodings(image):
    """비동기적으로 얼굴 인코딩 처리 - 메모리 관리 개선"""
    if image is None:
        logger.error("입력 이미지가 None입니다")
        return []
    
    try:
        loop = asyncio.get_event_loop()
        
        # 이미지 크기 조정
        height, width = image.shape[:2]
        max_dimension = 1300
        
        if max(height, width) > max_dimension:
            scale = max_dimension / max(height, width)
            # RGB로 변환하여 처리
            image = await loop.run_in_executor(
                THREAD_POOL,
                lambda: cv2.cvtColor(cv2.resize(image, None, fx=scale, fy=scale), cv2.COLOR_BGR2RGB)
            )
        else:
            # RGB로 변환
            image = await loop.run_in_executor(
                THREAD_POOL,
                lambda: cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
            )

        # numpy array를 연속적인 메모리에 할당
        image = np.ascontiguousarray(image)

        # 얼굴 검출 시도 - 파라미터 조정
        face_locations = await loop.run_in_executor(
            THREAD_POOL,
            lambda: face_recognition.face_locations(
                image, 
                model="hog",
                number_of_times_to_upsample=1
            )
        )
        
        if not face_locations:
            del image
            return []
            
        # 메모리 사용량 최적화를 위한 배치 처리
        batch_size = 5
        face_encodings = []
        
        for i in range(0, len(face_locations), batch_size):
            batch_locations = face_locations[i:i + batch_size]
            batch_encodings = await loop.run_in_executor(
                THREAD_POOL,
                lambda: face_recognition.face_encodings(
                    image,
                    batch_locations,
                    num_jitters=2,  # jitter 값 감소
                    model="small"  # 더 작은 모델 사용
                )
            )
            face_encodings.extend(batch_encodings)
            
            # 배치 처리 후 메모리 정리
            del batch_encodings
        
        # 메모리 정리
        del image
        del face_locations
        
        return face_encodings
            
    except Exception as e:
        logger.error(f"얼굴 인코딩 중 에러 발생: {str(e)}")
        return []
    finally:
        # 추가적인 메모리 정리
        if 'image' in locals():
            del image

@app.post("/face-recognition/classification", response_model=BaseResponse[List[SimilarityResponse]])
async def classify_images(request: AnalysisRequest):
    """
    여러 이미지에서 검출된 얼굴들 중 각 인물별 최대 유사도를 분석합니다.
    """
    return await adaptive_processing(_classify_images, request)

async def _classify_images(request: AnalysisRequest):
    start_time = time.time()
    try:
        async with aiohttp.ClientSession() as session:
            # 인물 이미지 처리
            people_images = []
            for person in request.people:
                img = await load_image_from_url_async(person.photoUrl, session)
                if img is None:
                    return BaseResponse.create(
                        status_code=400,
                        message=f"인물 이미지를 로드할 수 없습니다: {person.photoUrl}"
                    )
                people_images.append(img)

            # 인물 얼굴 인코딩
            people_encodings_list = []
            for img in people_images:
                encodings = await process_face_encodings(img)
                if not encodings:
                    logger.warning("인물 이미지에서 얼굴을 찾을 수 없습니다")
                    encodings = []
                people_encodings_list.append(encodings)

            people_encodings = {
                person.id: encodings[0] if encodings and len(encodings) > 0 else None
                for person, encodings in zip(request.people, people_encodings_list)
            }

            # 대상 이미지를 배치로 나누어 처리
            BATCH_SIZE = 10
            results = []
            
            for i in range(0, len(request.targetImages), BATCH_SIZE):
                batch_urls = request.targetImages[i:i+BATCH_SIZE]
                
                batch_images = []
                for url in batch_urls:
                    img = await load_image_from_url_async(url, session)
                    if img is None:
                        logger.warning(f"이미지 로드 실패: {url}")
                        continue
                    batch_images.append((url, img))

                for url, img in batch_images:
                    target_encodings = await process_face_encodings(img)
                    face_count = len(target_encodings)
                    max_similarities = {person_id: 0.0 for person_id in people_encodings.keys()}

                    if target_encodings:
                        for target_encoding in target_encodings:
                            for person_id, person_encoding in people_encodings.items():
                                if person_encoding is not None:
                                    try:
                                        similarity = await asyncio.get_event_loop().run_in_executor(
                                            THREAD_POOL,
                                            lambda: face_recognition.face_distance([person_encoding], target_encoding)[0]
                                        )
                                        similarity = max(0, 1 - similarity)
                                        max_similarities[person_id] = max(
                                            max_similarities[person_id],
                                            float(similarity)
                                        )
                                    except Exception as e:
                                        logger.error(f"유사도 계산 중 에러: {str(e)}")
                                        continue

                    results.append(SimilarityResponse(
                        imageUrl=url,
                        similarities=max_similarities,
                        faceCount=face_count
                    ))

            response = BaseResponse.create(
                status_code=200,
                message="얼굴 유사도 분석이 완료되었습니다.",
                data=results if results else []
            )

            logger.info(f"Classification API 응답:\n{response.to_json_log()}")
            execution_time = time.time() - start_time
            logger.info(f"Classification API 처리 시간: {execution_time:.2f}초")
            return response

    except Exception as e:
        execution_time = time.time() - start_time
        error_response = BaseResponse.create(
            status_code=500,
            message=f"얼굴 유사도 분석 중 오류가 발생했습니다: {str(e)}"
        )
        logger.error(f"Classification API 에러 응답:\n{error_response.to_json_log()}")
        logger.error(f"Classification API 처리 시간: {execution_time:.2f}초")        
        return error_response

@app.post("/face-recognition/face-count", response_model=BaseResponse[CountResponse])
async def count_faces(file: UploadFile = File(...)):
    return await adaptive_processing(_count_faces, file)

async def _count_faces(file: UploadFile):
    start_time = time.time()
    try:
        logger.info(f"얼굴 수 검출 요청 시작 - 파일명: {file.filename}")
        
        allowed_extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"}
        file_ext = file.filename.lower()[file.filename.rfind("."):]
        
        if file_ext not in allowed_extensions:
            return BaseResponse.create(
                status_code=400,
                message="지원되지 않는 파일 형식입니다. JPG, JPEG, PNG, GIF, BMP, WEBP 파일만 허용됩니다."
            )
        
        MAX_FILE_SIZE = 20 * 1024 * 1024 
        file_content = await file.read()
        file_size = len(file_content)
        
        if file_size > MAX_FILE_SIZE:
            return BaseResponse.create(
                status_code=400,
                message="파일 크기가 너무 큽니다. 최대 20MB까지 허용됩니다."
            )

        img = await load_image_from_bytes(file_content)
        
        if img is None:
            return BaseResponse.create(
                status_code=400,
                message="이미지를 처리할 수 없습니다."
            )
        
        face_encodings = await process_face_encodings(img)
        face_count = len(face_encodings)
        
        response = BaseResponse.create(
            status_code=200,
            message="얼굴 수 검출이 완료되었습니다.",
            data=CountResponse(faceCount=face_count)
        )
        
        logger.info(f"Face Count API 응답:\n{response.to_json_log()}")
        execution_time = time.time() - start_time
        logger.info(f"Face Count API 처리 시간: {execution_time:.2f}초")
        return response
        
    except Exception as e:
        execution_time = time.time() - start_time
        error_response = BaseResponse.create(
            status_code=500,
            message=f"얼굴 수 검출 중 오류가 발생했습니다: {str(e)}"
        )
        logger.error(f"Face Count API 에러 응답:\n{error_response.to_json_log()}")
        logger.error(f"Face Count API 처리 시간: {execution_time:.2f}초")
        return error_response
    
if __name__ == "__main__":
    import uvicorn
    logger.info(f"Starting server on port {SERVER_PORT}")
    uvicorn.run(
        app, 
        host=SERVER_HOST,
        port=SERVER_PORT,
    )