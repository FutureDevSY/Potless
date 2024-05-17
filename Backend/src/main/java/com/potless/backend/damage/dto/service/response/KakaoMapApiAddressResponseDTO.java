package com.potless.backend.damage.dto.service.response;

import com.potless.backend.damage.dto.service.response.kakaoAddress.Document;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoMapApiAddressResponseDTO {
    private List<Document> documents;
}
