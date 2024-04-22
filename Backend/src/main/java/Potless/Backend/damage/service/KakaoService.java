package Potless.Backend.damage.service;

import Potless.Backend.damage.dto.service.response.KakaoMapApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private static final String SSAFY_BANK_API_SERVER = System.getenv("KAKAO_API_SERVICE_KEY");
    private final RestTemplate restTemplate;

    @Async
    public CompletableFuture<KakaoMapApiResponseDTO> fetchKakaoData(Double x, Double y) {
        // Set up the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + SSAFY_BANK_API_SERVER);
        // Build the URL
        String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com/v2/local/geo/coord2address.json")
                .queryParam("x", "{x}")
                .queryParam("y", "{y}")
                .encode()
                .toUriString();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoMapApiResponseDTO> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.POST,
                entity,
                KakaoMapApiResponseDTO.class,
                x, y
        );

        return CompletableFuture.completedFuture(response.getBody());
    }
}
