# 사용할 베이스 이미지 지정
FROM python:3.9-slim

# 환경 변수 설정
ENV PROJECT_NAME=fastApiServer \
    BACKEND_CORS_ORIGINS='["http://localhost:7080", "https://api.potless.co.kr"]' \
    MODEL_PATH='' \
    MODEL_NAME='' \
    DATASET_PATH='' \
    DATASET_NAME='' \
    ENCODING=UTF-8 \
    LOG_LEVEL=INFO \
    SPRING_BASE_URL=https://api.potless.co.kr \
    PYTHONPATH=/app/app

# 작업 디렉토리 설정
WORKDIR /app

# 필수 패키지 설치
RUN apt-get update && apt-get install -y libgl1-mesa-glx libglib2.0-0

# pip 업데이트
RUN pip install --upgrade pip

# 의존성 파일들을 컨테이너로 복사
COPY requirements.txt /app/

# 의존성 설치
RUN pip install --no-cache-dir -r requirements.txt

# 애플리케이션 코드를 컨테이너로 복사
COPY . /app/

# Uvicorn 서버를 사용하여 애플리케이션 실행
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--reload"]
