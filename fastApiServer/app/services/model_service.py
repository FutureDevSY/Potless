import torch
import cv2
import os
import numpy as np
from ultralytics import YOLO
from PIL import Image
import logging
from dto.data_class import DetectionResponse, Box
from services.estimate_service import calcPotholeDan, calcPotholeWidth
from fastapi import APIRouter, HTTPException

def model_2th_detection(image):

    model = YOLO('./model/best.pt')
    # 감지 실행
    results = model.predict(task="detect", source=image, stream=False, conf=0.75)

    folder_name = 'model_result/result1'
    os.makedirs(folder_name, exist_ok=True)
    base_filename = os.path.basename(image)

    danger_max = -1
    i = 1
    boxes = []

    # 결과에서 감지된 객체 처리
    for result in results:
        if result.boxes:
            for box in result.boxes:
                i += 1

                # 탐지된 박스가 여러개일 경우를 고려
                box_x, box_y, box_width, box_height = box.xywh[0]
                boxes.append(Box(x=box_x, y=box_y, width=box_width, height=box_height))

                widthResult = calcPotholeWidth(box_y=box_y, box_width=box_width).item();
                logging.info("widthResult: " + str(widthResult))
                width = round(widthResult * 100, 2)
                logging.info("widthResultToCmAndRound: " + str(width))

                severity = calcPotholeDan(pothole_width=width, box_x=box_x, box_y=box_y, box_width=box_width)
                if(severity > danger_max):
                    danger_max = severity
                    width_max = width

        else:
            logging.info("포트홀 탐지 실패")
            return None, None
        
    boxed_file_path = os.path.join(folder_name, f"{base_filename}")
    result.save(filename=boxed_file_path)
      
    result = DetectionResponse(severity=danger_max, width=width_max)
    return result, boxes

    