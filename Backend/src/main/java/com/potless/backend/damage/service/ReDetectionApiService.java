package com.potless.backend.damage.service;
import com.potless.backend.damage.dto.service.request.ReDetectionRequestDTO;
import com.potless.backend.damage.dto.service.response.ReDetectionResponseDTO;
import com.potless.backend.global.exception.pothole.PotholeDetectionFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
@Service
@RequiredArgsConstructor
public class ReDetectionApiService {
    private final WebClient webClient = WebClient.builder()
//                                                 .baseUrl("https://ai.potless.co.kr")
                                                 .baseUrl("http://localhost:8000")
                                                 .build();

    /*
        1차 탐지된 포트홀 이미지 정보를 FASTAPI 서버에 전송해서 2차 재탐지 실행 및 처리결과 확인.
        탐지 성공한 경우 - 위험도 측정결과 응답
        탐지 실패한 경우 - 처리결과만 응답
     */
    public ReDetectionResponseDTO reDetectionResponse(ReDetectionRequestDTO requestDto) throws IOException {
        MultiValueMap<String, HttpEntity<?>> parts = new LinkedMultiValueMap<>();
        parts.add("image_data", new HttpEntity<>(new FileSystemResource(requestDto.getImage()), createFileHeaders(requestDto.getImage(), "image_data", "image/jpeg")));
        return webClient.post()
                        .uri("/api/detection")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData(parts))
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, response -> {
                            throw new PotholeDetectionFailException();
                        })
                        .bodyToMono(ReDetectionResponseDTO.class)
                        .block(); // Mono를 블로킹 호출하여 결과값을 얻음
    }
    private HttpHeaders createFileHeaders(File file, String name, String type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(type));
        headers.setContentLength(file.length());
        headers.setContentDispositionFormData(name, file.getName());
        return headers;
    }
}