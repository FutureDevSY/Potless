package com.potless.backend.damage.dto.service.response.kakao;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document {
    private RoadAddress road_address;
    private Address address;
}
