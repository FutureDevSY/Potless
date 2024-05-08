package com.potless.backend.damage.dto.service.response.kakaoAddress;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoadAddress {
    private String address_name;
    private String building_name;
    private String main_building_no;
    private String region_1depth_name;
    private String region_2depth_name;
    private String region_3depth_name;
    private String road_name;
    private String sub_building_no;
    private String underground_yn;
    private String x;
    private String y;
    private String zone_no;
}
