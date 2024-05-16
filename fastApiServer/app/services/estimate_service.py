thetas = [-0.4, 0.79, -0.6, -1.2, 1.185, 2.37] # 기울기
intercepts = [1.65, -0.15, 1.26, 0.86, 0.25, 0.05] # x절편

def calcPotholeWidth(box_y, box_width):
    x = [ 0, 0 ]
    for i in range(2):
        theta = thetas[i]
        intercept = intercepts[i]
        x[i] = (box_y + intercept * theta) / theta
    return 2 * box_width / (x[1] - x[0])

def calcPotholeDan(pothole_width, box_x, box_y, box_width):
    if pothole_width < 0.25: # 바퀴가 빠지지 않을 정도의 포트홀
        return 1
    
    boundary = [ 0, 0, 0, 0 ]
    for i in range(2, 6):
        theta = thetas[i]
        intercept = intercepts[i]
        boundary[i - 2] = (box_y + intercept * theta) / theta
    
    half_width = box_width / 2
    box_start = box_x - half_width
    box_end = box_x + half_width

    if (box_end < boundary[0] or box_start > boundary[3]): # 1번 6번에 위치한 포트홀
        return 1
    elif (box_start > boundary[1] and box_end < boundary[2]): # 3번 4번에 위치한 포트홀
        return 1
    elif (box_start > boundary[0] and box_end < boundary[1]): # 2번에 위치한 포트홀
        return 3
    elif (box_start > boundary[2] and box_end < boundary[3]): # 5번에 위치한 포트홀
        return 3
    elif (box_start < boundary[0] and box_end > boundary[1]): # 2번을 넘어가는 포트홀
        return 3
    elif (box_start < boundary[2] and box_end > boundary[3]): # 5번을 넘어가는 포트홀
        return 3
    else: # 2번 5번에 걸치는 포트홀
        return 2
    
# box_x = 0.25
# box_y = 0.6
# box_width = 0.05
# pothole_width = calcPotholeWidth(box_y, box_width)
# dangerousness = calcPotholeDan(pothole_width, box_x, box_y, box_width)

# print("pohtole_width: ", pothole_width)
# print("dangerousness: ", dangerousness)