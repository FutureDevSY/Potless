from fastapi import APIRouter, HTTPException
from fastapi.responses import JSONResponse, StreamingResponse
from typing import List
import cv2
import os
import numpy as np
from datetime import datetime
from services.augmentation_service import process_images
from services.model_service import model_2th_detection
from services.estimate_service import calcPotholeDan, calcPotholeWidth
from services.calculate_service import calculate_object_scale
from fastapi import FastAPI, File, UploadFile
from dto.data_class import DetectionResponse
import io
from fastapi.responses import FileResponse
import logging


rec = APIRouter(
    tags=["detection"],
    responses={404: {"description": "Page Not found"}},
)


# 2차 탐지, 1차적으로 탐지된 포트홀을 2차 검증 
@rec.post("/detection", status_code=200, 
            summary="포트홀 2차 탐지 및 위험도 분석 요청 API",
            response_description="2차 탐지로 확인된 포트홀 이미지 데이터, 결과 메시지 반환 ", 
            description="재탐지된 결과에 대해서는 탐지된 이미지 데이터와 위험도 관련 정보를 함께 전송, 아니라면 결과와 메시지만 반환합니다.")
async def detection_confirm(
    image_data: UploadFile = File(...)
):
    # 파일 확장자 추출
    file_extension = image_data.filename.split('.')[-1]
    current_time = datetime.now().strftime("%Y%m%d_%H%M%S")
    file_name = f"{current_time}.{file_extension}"

    # 업로드된 파일을 바이트 배열로 읽기
    origin_image = await image_data.read()
    
    # 바이트 배열을 numpy 배열로 변환
    np_image = np.frombuffer(origin_image, np.uint8)
    saved = cv2.imdecode(np_image, cv2.IMREAD_COLOR)

    # 이미지 저장
    folder_name = 'origin'
    if not os.path.exists(folder_name):
        os.makedirs(folder_name)

    origin_file_path = os.path.join(folder_name, f"{file_name}")
    cv2.imwrite(origin_file_path, saved)

    logging.info("파일 받고 원본사진 저장 완료")

    # processed_image = await process_images(origin_file_path)
    detection_result = model_2th_detection(origin_file_path)
    logging.info("2-1차 processing 완료")

    if(detection_result is None):
        # 전처리 뒤 2-2차 탐지 실행  
        processed_image = await process_images(origin_file_path)
        if(processed_image is None):
            raise HTTPException(status_code=401, detail="전처리 실패 - 입력 이미지가 올바르지 않습니다.")
       
        detection_result = model_2th_detection(processed_image)
        logging.info("2-2차 processing 완료")

        if(detection_result is None):
            raise HTTPException(status_code=401, detail="2차 프로세싱 모두 탐지하지 못했습니다.")
         
    logging.info("2차 탐지결과 확인 및 위험도 분석 완료")

    return detection_result
    
    # def iterfile():
    #     with open(processed_image_path, mode="rb") as file_like:
 #         yield from file_like

    # response = StreamingResponse(iterfile(), media_type="image/jpeg")
    # return {"message": "처리가 성공적으로 완료되었습니다.", "data": response}
