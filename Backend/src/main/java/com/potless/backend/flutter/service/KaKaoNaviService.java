package com.potless.backend.flutter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potless.backend.flutter.dto.service.response.DamageAppResponseDTO;
import com.potless.backend.damage.repository.DamageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KaKaoNaviService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final DamageRepository damageRepository;

    @Value("${kakao.api-service-key}")
    private String KAKAO_API_KEY;

    @Value("${python.server.url}")
    private String pythonServerUrl = "https://math.potless.co.kr";

    @Async
    public CompletableFuture<String> fetchKakaoData(Double startX, Double startY, Double endX, Double endY) {
        return CompletableFuture.supplyAsync(() -> {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

            // Build the URL
            String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://apis-navi.kakaomobility.com/v1/directions")
                    .queryParam("origin", startX + "," + startY)
                    .queryParam("destination", endX + "," + endY)
                    .encode()
                    .toUriString();

            HttpEntity<String> entity = new HttpEntity<>(headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        urlTemplate,
                        HttpMethod.GET,
                        entity,
                        String.class
                );
                return response.getBody();
            } catch (Exception e) {
                log.error("Error fetching Kakao navigation data", e);
                return null;
            }
        });
    }

    public CompletableFuture<List<String>> processKakaoData(String kakaoResponse) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");

                String url = pythonServerUrl + "/processKakaoData";

                Map<String, Object> requestPayload = new HashMap<>();
                requestPayload.put("kakaoResponseDTO", objectMapper.readValue(kakaoResponse, Map.class));

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    // H3 인덱스를 JSON에서 객체로 변환
                    return objectMapper.readValue(response.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
                } else {
                    log.error("Error from Python server: " + response.getStatusCode());
                }
            } catch (Exception e) {
                log.error("Error processing Kakao data", e);
            }
            return null;
        });
    }

    @Async
    public CompletableFuture<List<DamageAppResponseDTO>> checkCoordinates(List<String> hexagonIndexes) {
        return CompletableFuture.supplyAsync(() -> {
            if (hexagonIndexes == null || hexagonIndexes.isEmpty()) {
                return List.of();
            }
            return damageRepository.findByHexagonIndexIn(hexagonIndexes);
        });
    }
}
