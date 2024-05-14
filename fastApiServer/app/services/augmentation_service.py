import os
import cv2
import numpy as np
import math
from fastapi import UploadFile, File
import logging


# before_path = "./before/"
# after_path = "./after/"
# label_path = "./label/"


async def color_inverse(image_data: UploadFile):

    # image_bytes = await image_data.read()  # 파일의 바이트 데이터를 읽습니다.
    image_array = np.frombuffer(image_data, np.uint8)  # 바이트 데이터를 NumPy 배열로 변환합니다.
    gray = cv2.imdecode(image_array, cv2.IMREAD_GRAYSCALE)  # NumPy 배열을 이미지 데이터로 디코드합니다.

    height, width = gray.shape
    pixel_num = pixel_sum = 0
     # label_data가 bytes형태이므로 문자열로 디코딩
    # label_str = label.decode('utf-8')
    # label_info = label_str.strip().split()
    # labels = label_str.readlines()
    # labels = label_str.strip().split('\n')

    for label in labels:
        label_info = label.split()
        type, center_x, center_y, box_width, box_height = label_info
    
        if type != '0':
            return None
        center_x = float(center_x) * width
        center_y = float(center_y) * height
        box_half_width = float(box_width) * width / 2
        box_half_height = float(box_height) * height / 2
        left_top = [int(center_x - box_half_width), int(center_y - box_half_height)]
        right_bottom = [int(center_x + box_half_width), int(center_y + box_half_height)]

        pixel_num = pixel_num + (right_bottom[1] - left_top[1]) * (right_bottom[0] - left_top[0])
        pixel_sum = pixel_sum + gray[left_top[1]:right_bottom[1], left_top[0]:right_bottom[0]].sum()
    
    pixel_avg = pixel_sum / pixel_num
    if pixel_avg > 100:
        color = cv2.cvtColor(gray, cv2.COLOR_GRAY2BGR)
        logging.info("avg > 100")
        return await process_images(color)
    
    logging.info("avg <= 100")
    return await process_images(gray)



async def process_images(image_path):

    color = cv2.imread(image_path, cv2.IMREAD_COLOR)

    if color is None:
        logging.info("이미지 전처리 실패 - 이미지 입력값이 올바르지 않습니다.")
        return None
    
    # 이미지를 그레이스케일로 변환
    gray = cv2.cvtColor(color, cv2.COLOR_BGR2GRAY)

    height, width, channels = color.shape
    m = max(map(max, gray))
    c = 255 / math.log(1 + m)
    GAMMA = 2.5
    ALPHA = 1.0

    # Perform log and gamma correction using vectorized operations
    contrast = np.clip((1 + ALPHA) * color - 128 * ALPHA, 0, 255).astype(np.uint8)
    basic = c * np.log(1 + contrast.astype(np.float32))
    basic = c * ((basic / 255) ** GAMMA)

    # Convert to grayscale using OpenCV
    grayscale = cv2.cvtColor(basic, cv2.COLOR_BGR2GRAY).astype(np.uint8)

    # Superpixel creation
    seeds = cv2.ximgproc.createSuperpixelSEEDS(width, height, channels, 500, 4, 5, 5)
    seeds.iterate(cv2.cvtColor(contrast, cv2.COLOR_BGR2HSV), 10)
    mask = seeds.getLabelContourMask(False)
    inversed_mask = cv2.bitwise_not(mask)
    background = cv2.bitwise_and(contrast, contrast, mask=inversed_mask)
    foreground = cv2.bitwise_and(125 * np.ones_like(color, np.uint8), 125 * np.ones_like(color, np.uint8), mask=mask)
    superpixel = cv2.add(background, foreground)

    # Edge detection using Sobel
    medianBlurred = cv2.medianBlur(grayscale, 7)
    sobel_x = cv2.Sobel(medianBlurred, cv2.CV_64F, 1, 0, ksize=3)
    sobel_y = cv2.Sobel(medianBlurred, cv2.CV_64F, 0, 1, ksize=3)
    sobel = cv2.addWeighted(cv2.convertScaleAbs(sobel_x), 5, cv2.convertScaleAbs(sobel_y), 5, 0)

    # Composing final image
    compose = np.dstack([superpixel[:, :, 1], grayscale, sobel])

    # 이미지 처리 후 저장
    processed_image_path = 'processed_image.jpg'
    cv2.imwrite(processed_image_path, compose.astype(np.uint8))

    return processed_image_path



# async def process_images(image):
#     # 가정: 이미지 처리 후 파일 시스템에 저장
#     cv2.imwrite('processed_image.jpg', image)
#     return 'processed_image.jpg'