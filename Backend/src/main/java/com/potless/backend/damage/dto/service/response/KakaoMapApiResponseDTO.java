package com.potless.backend.damage.dto.service.response;

import com.potless.backend.damage.dto.service.response.kakao.Document;
import com.potless.backend.damage.dto.service.response.kakao.Meta;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoMapApiResponseDTO {

    private Meta meta;
    private List<Document> documents;

}
