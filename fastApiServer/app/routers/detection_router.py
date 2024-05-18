from fastapi import APIRouter, HTTPException, File, UploadFile
from fastapi.responses import JSONResponse, StreamingResponse
from typing import List
import cv2
import os
import numpy as np
from datetime import datetime
from services.augmentation_service import process_images
from services.model_service import model_2th_detection
from services.estimate_service import calcPotholeDan, calcPotholeWidth
from services.calculate_service import calculate_object_scale, box_drawer
from services.aws_service import upload_file_to_s3
from fastapi import FastAPI, File, UploadFile
from dto.data_class import DetectionResponse
from routers.aws_router import upload_image
import io
import logging
from fastapi import FastAPI, File, UploadFile
import os
from typing import Dict
from datetime import datetime


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

    logging.info("파일 받고 원본사진 프로젝트 내 저장 완료")
    return_result = None

    #  1차 시도 - 원본사진으로 프로세싱 ------------------------------------------------------------
    # detection_result, boxes, file_url = model_2th_detection(origin_file_path)
    # logging.info("2-1차 processing 완료 - 원본만")
    # if(detection_result is not None):
    #     return_result = detection_result
    #     # 원본에서 탐지된 이미지 aws에 저장 
    #     return_url = await upload_image(file_url)


    #  2차 시도 - 전처리만 한걸로 프로세싱 ---------------------------------------------------------

    processed_image = await process_images(origin_file_path, 1) 
    detection_result, boxes = model_2th_detection(processed_image)
    logging.info("2-1차 processing 완료 - 전처리만")

    if(detection_result is not None):
        return_result = detection_result
        url = box_drawer(origin_image_path=origin_file_path, boxes=boxes)
        # 원본에서 박스 그린 이미지를 aws에 저장
        return_url = await upload_image(url)
    

    #  3차 시도 - 전처리 + 반전으로 프로세싱 ------------------------------------------------------
    if(return_result is None):
        processed_image = await process_images(origin_file_path, 2)
        if(processed_image is None):
            raise HTTPException(status_code=401, detail="전처리 실패 - 입력 이미지가 올바르지 않습니다.")
       
        detection_result, boxes = model_2th_detection(processed_image)
        logging.info("2-2차 processing 완료 - 전처리 + 반전")
        
        if(detection_result is None):
            raise HTTPException(status_code=401, detail="2차 프로세싱 모두 탐지하지 못했습니다.")

        else:
            return_result = detection_result
            url = box_drawer(origin_image_path=origin_file_path, boxes=boxes)
            return_url = await upload_image(url)

            
         
    logging.info("2차 탐지결과 확인 및 위험도 분석 완료")

    return return_result, return_url




# --------------------------------------------------------------------------------------------------------------------------

# @rec.post("/upload/")
# async def upload_image(image_path: str):
#     base_filename = os.path.basename(image_path)
#     image = cv2.imread(image_path)
#     # 파일 이름 설정
#     object_name = f"AfterVerification/BeforeWork/{base_filename}"

#     # S3에 파일 업로드
#     result = upload_file_to_s3(image, object_name)
#     logging.info("박스 이미지 파일 AWS 업로드 완료")
#     return result


# @rec.post("/detection", status_code=200, 
#             summary="포트홀 2차 탐지 및 위험도 분석 요청 API",
#             response_description="2차 탐지로 확인된 포트홀 이미지 데이터, 결과 메시지 반환 ", 
#             description="재탐지된 결과에 대해서는 탐지된 이미지 데이터와 위험도 관련 정보를 함께 전송, 아니라면 결과와 메시지만 반환합니다.")
# async def detection_confirm(
#     image_data: UploadFile = File(...)
# ):
#     # 파일 확장자 추출
#     file_extension = image_data.filename.split('.')[-1]
#     current_time = datetime.now().strftime("%Y%m%d_%H%M%S")
#     file_name = f"{current_time}.{file_extension}"

#     # 업로드된 파일을 바이트 배열로 읽기
#     origin_image = await image_data.read()
    
#     # 바이트 배열을 numpy 배열로 변환
#     np_image = np.frombuffer(origin_image, np.uint8)
#     saved = cv2.imdecode(np_image, cv2.IMREAD_COLOR)

#     # 이미지 저장
#     folder_name = 'origin'
#     if not os.path.exists(folder_name):
#         os.makedirs(folder_name)

#     origin_file_path = os.path.join(folder_name, f"{file_name}")
#     cv2.imwrite(origin_file_path, saved)

#     logging.info("파일 받고 원본사진 저장 완료")

#     # 1. 원본 
#     detection_result = model_2th_detection(origin_file_path)
#     if(detection_result is not None):
#         logging.info("1 - 원본 탐지 성공")

#     # 2. 전처리만
#     processed_image = await process_images(origin_file_path, 1)
#     detection_result = model_2th_detection(processed_image)
#     if(detection_result is not None):
#         logging.info("2 - 전처리만 탐지 성공")

#     # 3. 전처리 + 반전 둘다
#     processed_image = await process_images(origin_file_path, 2)
#     detection_result = model_2th_detection(processed_image)
#     if(detection_result is not None):
#         logging.info("3 - 전처리 + 반전 탐지 성공")

#     logging.info("테스트 완료")

#     return detection_result

