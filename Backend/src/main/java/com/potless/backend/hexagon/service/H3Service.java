package com.potless.backend.hexagon.service;

import com.uber.h3core.H3Core;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class H3Service {

    private final H3Core h3Core;

    public H3Service() throws IOException {
        this.h3Core = H3Core.newInstance();
    }

    public String getH3Index(double latitude, double longitude, int resolution) {
        long h3Index = h3Core.geoToH3(latitude, longitude, resolution);
        return Long.toHexString(h3Index); // long 타입의 H3 인덱스를 16진수 문자열로 변환
    }
}
