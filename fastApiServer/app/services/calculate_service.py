import cv2
import os
import numpy as np
from routers.aws_router import upload_image

def box_drawer(origin_image_path, boxes):
    #  원본 이미지 읽어오기 
    image = cv2.imread(origin_image_path)

    for box in boxes:
        x, y, w, h = box.x, box.y, box.width, box.height
        xmin = int(x - w / 2)
        ymin = int(y - h / 2)
        xmax = int(x + w / 2)
        ymax = int(y + h / 2)

        cv2.rectangle(image, (xmin, ymin), (xmax, ymax), (0, 0, 255), 2)

    # cv2.imshow("Image with Boxes", image)
    # cv2.waitKey(0)
    # cv2.destroyAllWindows()

    folder_name = 'origin_boxed'
    if not os.path.exists(folder_name):
        os.makedirs(folder_name)
    base_filename = os.path.basename(origin_image_path)

    boxed_file_path = os.path.join(folder_name, f"{base_filename}")
    cv2.imwrite(boxed_file_path, image)

    # url = upload_image(boxed_file_path)
    return boxed_file_path










def calculate_object_scale(image_path):
    image = cv2.imread(image_path)
    hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    # # 빨간색 범위 정의
    # lower_red = np.array([0, 120, 70])
    # upper_red = np.array([10, 255, 255])
    # mask1 = cv2.inRange(hsv, lower_red, upper_red)
    #
    # lower_red = np.array([170, 120, 70])
    # upper_red = np.array([180, 255, 255])
    # mask2 = cv2.inRange(hsv, lower_red, upper_red)

    # RGB (255, 0, 0)에 대응하는 빨간색 범위를 HSV로 설정
    lower_red1 = np.array([0, 150, 150])  # H 범위를 좁혀 정확도를 높임
    upper_red1 = np.array([10, 255, 255])
    mask1 = cv2.inRange(hsv, lower_red1, upper_red1)

    lower_red2 = np.array([170, 150, 150])
    upper_red2 = np.array([180, 255, 255])
    mask2 = cv2.inRange(hsv, lower_red2, upper_red2)

    # 두 마스크를 결합하여 빨간색 마스크 생성
    red_mask = cv2.bitwise_or(mask1, mask2)

    # 윤곽선 검출
    contours, _ = cv2.findContours(red_mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    max_area_rect = None
    max_area = 0
    count_red_boxes = len(contours)  # 빨간색 사각형의 수

    # 가장 큰 빨간 사각형의 윤곽선 찾기
    for contour in contours:
        rect = cv2.boundingRect(contour)
        area = rect[2] * rect[3]
        if area > max_area:
            max_area = area
            max_area_rect = rect

    if max_area_rect is not None:
        # 가장 큰 사각형에 초록색 사각형 그리기
        cv2.rectangle(image, (max_area_rect[0], max_area_rect[1]),
                      (max_area_rect[0] + max_area_rect[2], max_area_rect[1] + max_area_rect[3]),
                      (0, 255, 0), 2)

        # 하단부와 연결하는 파란색 선 그리기
        bottom_of_rectangle = max_area_rect[1] + max_area_rect[3]
        cv2.line(image, (max_area_rect[0] + max_area_rect[2]//2, bottom_of_rectangle),
                 (max_area_rect[0] + max_area_rect[2]//2, image.shape[0]), (255, 0, 0), 2)

        # # 가중치 계산
        # distance_from_bottom = image.shape[0] - bottom_of_rectangle

        # 가중치 계산 (제곱근을 사용하여 부드럽게)
        distance_from_bottom = image.shape[0] - bottom_of_rectangle
        weight = np.sqrt(distance_from_bottom / image.shape[0])

        print("Number of red boxes detected: {count_red_boxes}")
        print("Distance from bottom:", distance_from_bottom)
        print("Box width in pixels:", max_area_rect[2])
        print("Adjusted value:", weight * max_area_rect[2])

        # 이미지 축소
        scale_percent = 50
        width = int(image.shape[1] * scale_percent / 100)
        height = int(image.shape[0] * scale_percent / 100)
        dim = (width, height)
        resized_image = cv2.resize(image, dim, interpolation=cv2.INTER_AREA)

        # 결과 이미지 표시
        cv2.imshow('Detected Largest Rectangle', resized_image)
        cv2.waitKey(0)
        cv2.destroyAllWindows()
    else:
        print("No red rectangle found.")

    # 결과값에 따라 위험도 등급을 나눌 범위를 세워야 할 듯. 100~150 / 150~200 / 200 이상 이런식으로?



# ////////////////////////////////////////////////////////////////////////////////////

# 박스 6: (겹치는 사각형 두개가 하나로 묶임 + 동떨어진 사각형 각각 두개 있는 사진)
# Number of red boxes detected: 3
# Distance from bottom: 509
# Box width in pixels: 330
# Adjusted value: 235.55378491033758

# 박스 5: (겹치는 사각형 두개가 하나로 묶임 + 동떨어진 사각형 하나 있는 사진)
# Number of red boxes detected: 2
# Distance from bottom: 505
# Box width in pixels: 330
# Adjusted value: 235.21577062883154


# 박스4: (겹치는 사각형 두개만 있는 사진)
# Number of red boxes detected: 1
# Distance from bottom: 502
# Box width in pixels: 330
# Adjusted value: 235.4655308684915

# 박스3:
# Number of red boxes detected: 1
# Distance from bottom: 544
# Box width in pixels: 208
# Adjusted value: 154.26401803355972

# 박스2:
# Number of red boxes detected: 1
# Distance from bottom: 611
# Box width in pixels: 224
# Adjusted value: 178.4249073411816

# 박스1:
# Number of red boxes detected: 1
# Distance from bottom: 259
# Box width in pixels: 232
# Adjusted value: 120.25394982341653