package com.potless.backend.flutter.dto.controller.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlutterSendRequestDTO {

    private Double startX;
    private Double startY;

    private Double endX;
    private Double endY;

    @Builder
    public FlutterSendRequestDTO(Double startX, Double startY, Double endX, Double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
}
