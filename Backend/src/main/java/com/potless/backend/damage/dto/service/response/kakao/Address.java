package com.potless.backend.damage.dto.service.response.kakao;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String address_name;
    private String region_1depth_name;
    private String region_2depth_name;
    private String region_3depth_name;
    private String mountain_yn;
    private String main_address_no;
    private String sub_address_no;
    private String zip_code;
}
