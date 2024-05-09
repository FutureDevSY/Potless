package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.service.request.ReDetectionRequestDTO;
import com.potless.backend.damage.dto.service.response.ReDetectionErrorResponseDto;
import com.potless.backend.damage.dto.service.response.ReDetectionResponseDTO;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.global.exception.member.InvalidLoginAuthException;
import com.potless.backend.global.exception.pothole.PotholeDetectionFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static reactor.core.publisher.Mono.just;

@Service
@RequiredArgsConstructor
public class ReDetectionApiService {

    private final WebClient webClient = WebClient.builder()
                                                 .baseUrl("http://localhost:8000")
                                                 .build();

    /*
        1차 탐지된 포트홀 이미지 정보를 FASTAPI 서버에 전송해서 2차 재탐지 실행 및 처리결과 확인.
        탐지 성공한 경우 - 위험도 측정결과 응답
        탐지 실패한 경우 - 처리결과만 응답
     */
    public Integer reDetectionResponse(ReDetectionRequestDTO requestDto) throws IOException {
        MultiValueMap<String, HttpEntity<?>> parts = new LinkedMultiValueMap<>();
        parts.add("image_data", new HttpEntity<>(new ByteArrayResource(requestDto.getImage().getBytes()), createFileHeaders(requestDto.getImage(), "image_data")));
        parts.add("label_data", new HttpEntity<>(new ByteArrayResource(requestDto.getLabel().getBytes()), createFileHeaders(requestDto.getLabel(), "label_data")));

        return webClient.post()
                        .uri("/api/detection")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData(parts))
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, response -> {
                            throw new PotholeDetectionFailException();
                        })
                        .bodyToMono(ReDetectionResponseDTO.class)
                        .map(ReDetectionResponseDTO::getSeverity) // 성공 응답시 severity 값 반환
                        .block(); // Mono를 블로킹 호출하여 결과값을 얻음
    }

    private HttpHeaders createFileHeaders(MultipartFile file, String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        headers.setContentLength(file.getSize());
        headers.setContentDispositionFormData(name, file.getOriginalFilename());
        return headers;
    }


}
