package com.potless.backend.flutter.dto.controller.response;

import com.potless.backend.flutter.dto.service.response.DamageAppResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CombinedResponseDTO {

    private String kakaoResponse;
    private List<DamageAppResponseDTO> results;

    @Builder
    public CombinedResponseDTO(String kakaoResponse, List<DamageAppResponseDTO> results) {
        this.kakaoResponse = kakaoResponse;
        this.results = results;
    }
}
