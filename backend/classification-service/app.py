from fastapi import FastAPI, HTTPException, File, UploadFile
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import RedirectResponse
from pydantic import BaseModel, Field
from typing import List, Dict
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
SERVER_HOST = os.getenv('SERVER_HOST', '0.0.0.0')
INSTANCE_HOST = os.getenv('INSTANCE_HOST')

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def find_free_port():
    """사용 가능한 랜덤 포트를 찾는 함수"""
    with contextlib.closing(socket.socket(socket.AF_INET, socket.SOCK_STREAM)) as s:
        s.bind(('', 0))
        s.listen(1)
        port = s.getsockname()[1]
        return port

app = FastAPI(
    title="Face Classification API",
    description="얼굴 유사도 분석 API",
    version="1.0.0",
    docs_url=None,
    redoc_url=None
)

# 글로벌 변수로 포트 저장
server_port = None

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
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
        return None
    try:
        # 이미지 크기 조정
        height, width = image.shape[:2]
        max_dimension = 1024
        if max(height, width) > max_dimension:
            scale = max_dimension / max(height, width)
            image = cv2.resize(image, None, fx=scale, fy=scale)

        # HOG로 얼굴 검출
        face_locations = face_recognition.face_locations(
            image,
            model="hog",
            number_of_times_to_upsample=2  # 작은 얼굴도 찾기 위해 업샘플링
        )
        
        if not face_locations:
            # 첫 시도 실패시 다른 크기로 한번 더 시도
            scaled_image = cv2.resize(image, None, fx=0.5, fy=0.5)
            face_locations = face_recognition.face_locations(
                scaled_image,
                model="hog",
                number_of_times_to_upsample=2
            )
            if face_locations:
                # 좌표 원본 크기로 변환
                face_locations = [(int(top*2), int(right*2), 
                                 int(bottom*2), int(left*2))
                                for top, right, bottom, left in face_locations]
        
        if not face_locations:
            return None
            
        # 얼굴 인코딩
        face_encodings = face_recognition.face_encodings(
            image,
            face_locations,
            num_jitters=1  # 기본값 사용
        )
        
        return face_encodings if face_encodings else None
        
    except Exception as e:
        logger.error(f"얼굴 인코딩 실패: {str(e)}")
        return None

@app.get("/", include_in_schema=False)
async def root():
    return RedirectResponse(url="/docs")

@app.get("/docs", include_in_schema=False)
async def custom_swagger_ui_html():
    from fastapi.openapi.docs import get_swagger_ui_html
    return get_swagger_ui_html(
        openapi_url=app.openapi_url,
        title=app.title + " - Swagger UI",
        swagger_favicon_url=""
    )

@app.get("/openapi.json", include_in_schema=False)
async def get_openapi_json():
    return app.openapi()

@app.post("/classification", response_model=List[SimilarityResponse])
async def classify_images(request: AnalysisRequest):
    """
    여러 이미지에서 검출된 얼굴들 중 각 인물별 최대 유사도를 분석합니다.

    - **targetImages**: 분석할 대상 이미지 URL 리스트
    - **people**: 비교할 사람들의 정보 (id와 photoUrl)

    Returns:
        각 이미지별로 등록된 사람들과의 최대 유사도 점수 (0~1 사이 값)
        얼굴이 감지되지 않는 경우 모든 유사도는 0으로 반환
    """
    # 등록된 사람들의 얼굴 인코딩 준비
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
    
    return results

# 기존 코드는 동일하고 /face-count API 부분만 수정됩니다

@app.post("/face-count", response_model=CountResponse)
async def count_faces(file: UploadFile = File(...)):
    """
    업로드된 이미지 파일에서 검출된 얼굴의 수를 반환합니다.

    - **file**: 이미지 파일 (multipart/form-data)
    - 지원 형식: JPG, JPEG, PNG, GIF, BMP, WEBP
    - 최대 파일 크기: 10MB

    Returns:
        검출된 얼굴의 수
    """
    # 파일 확장자 검사
    allowed_extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"}
    file_ext = file.filename.lower()[file.filename.rfind("."):]
    
    if file_ext not in allowed_extensions:
        raise HTTPException(
            status_code=400,
            detail="지원되지 않는 파일 형식입니다. JPG, JPEG, PNG, GIF, BMP, WEBP 파일만 허용됩니다."
        )
    
    # 파일 크기 제한 (10MB)
    MAX_FILE_SIZE = 10 * 1024 * 1024
    file_content = await file.read()
    if len(file_content) > MAX_FILE_SIZE:
        raise HTTPException(
            status_code=400,
            detail="파일 크기가 너무 큽니다. 최대 10MB까지 허용됩니다."
        )

    img = load_image_from_bytes(file_content)
    if img is None:
        raise HTTPException(status_code=400, detail="이미지를 처리할 수 없습니다")

    face_encodings = get_face_encodings(img)
    face_count = len(face_encodings) if face_encodings else 0

    return CountResponse(faceCount=face_count)


async def register_to_eureka():
    """Eureka 서버에 서비스 등록"""
    global server_port
    try:
        # eureka_client 설정
        eureka_config = {
            "eureka_server": EUREKA_SERVER,
            "app_name": APP_NAME,
            "instance_port": server_port,
            "instance_host": INSTANCE_HOST
        }
        
        # 설정값 로깅
        logger.info(f"Eureka configuration: {eureka_config}")
        
        # 필수 값 검증
        if not all([EUREKA_SERVER, APP_NAME, INSTANCE_HOST, server_port]):
            missing_values = []
            if not EUREKA_SERVER: missing_values.append("EUREKA_SERVER")
            if not APP_NAME: missing_values.append("APP_NAME")
            if not INSTANCE_HOST: missing_values.append("INSTANCE_HOST")
            if not server_port: missing_values.append("server_port")
            raise ValueError(f"Missing required configuration: {', '.join(missing_values)}")

        # Eureka 클라이언트 초기화
        await eureka_client.init_async(
            eureka_server=EUREKA_SERVER,
            app_name=APP_NAME,
            instance_port=server_port,
            instance_host=INSTANCE_HOST,
            instance_ip=INSTANCE_HOST
        )
        logger.info(f"Successfully registered to Eureka server at {EUREKA_SERVER}")
    except Exception as e:
        logger.error(f"Failed to register to Eureka server: {str(e)}")
        raise

@app.on_event("startup")
async def startup_event():
    """애플리케이션 시작 시 Eureka 서버에 등록"""
    global server_port
    if server_port is None:
        server_port = find_free_port()
    logger.info(f"Using port: {server_port}")
    await register_to_eureka()

@app.on_event("shutdown")
async def shutdown_event():
    """애플리케이션 종료 시 Eureka 서버에서 등록 해제"""
    try:
        await eureka_client.stop()
        logger.info("Successfully unregistered from Eureka server")
    except Exception as e:
        logger.error(f"Error during Eureka client shutdown: {str(e)}")

if __name__ == "__main__":
    import uvicorn
    server_port = find_free_port()
    logger.info(f"Starting server on port {server_port}")
    uvicorn.run(app, host=SERVER_HOST, port=server_port)