package com.potless.backend.damage.dto.service.response.kakaoAddress;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String address_name;

    private String b_code;
    private String h_code;
    private String main_address_no;
    private String mountain_yn;
    private String region_1depth_name;
    private String region_2depth_name;
    private String region_3depth_h_name;
    private String region_3depth_name;
    private String sub_address_no;
    private String x;
    private String y;

}
