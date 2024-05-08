package com.potless.backend.damage.dto.service.response;

import com.potless.backend.damage.dto.service.response.kakaoAddress.Document;
import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoMapApiAddressResponseDTO {
    private List<Document> documents;
}
