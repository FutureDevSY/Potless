package com.potless.backend.damage.dto.service.response.kakaoAddress;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document {

    private Address address;
    private String x;
    private String y;
}
