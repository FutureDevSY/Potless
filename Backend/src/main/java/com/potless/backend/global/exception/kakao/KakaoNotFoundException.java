package com.potless.backend.global.exception.kakao;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class KakaoNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public KakaoNotFoundException() {
        this.errorCode = ErrorCode.KAKAO_NOT_FOUND;
    }
}
