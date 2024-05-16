package com.potless.backend.flutter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.flutter.dto.service.response.DamageAppResponseDTO;
import com.potless.backend.flutter.dto.service.response.KakaoResponseDTO;
import com.potless.backend.flutter.dto.service.response.Point;
import com.potless.backend.hexagon.service.H3Service;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KaKaoNaviService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate;
    private final DamageRepository damageRepository;
    private final H3Service h3Service;

    @Value("${kakao.api-service-key}")
    private String KAKAO_API_KEY;

    @Async
    public CompletableFuture<String> fetchKakaoData(Double startX, Double startY, Double endX, Double endY) {
        // Set up the headers
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
            return CompletableFuture.completedFuture(response.getBody());
        } catch (Exception e) {
            log.error("Error fetching Kakao navigation data", e);
            return CompletableFuture.completedFuture(null);
        }
    }

    public List<Point> extractCoordinates(String kakaoResponse) throws Exception {
        KakaoResponseDTO kakaoResponseDTO = objectMapper.readValue(kakaoResponse, KakaoResponseDTO.class);
        List<Point> coordinates = new ArrayList<>();

        // Summary 부분에서 좌표 추출
        if (kakaoResponseDTO.getRoutes() != null) {
            for (KakaoResponseDTO.Route route : kakaoResponseDTO.getRoutes()) {
                if (route.getSummary() != null) {
                    addLocationToCoordinates(route.getSummary().getOrigin(), coordinates);
                    addLocationToCoordinates(route.getSummary().getDestination(), coordinates);
                    if (route.getSummary().getWaypoints() != null) {
                        for (KakaoResponseDTO.Location waypoint : route.getSummary().getWaypoints()) {
                            addLocationToCoordinates(waypoint, coordinates);
                        }
                    }
                    addBoundToCoordinates(route.getSummary().getBound(), coordinates);
                }

                // Sections 부분에서 좌표 추출
                if (route.getSections() != null) {
                    for (KakaoResponseDTO.Section section : route.getSections()) {
                        addBoundToCoordinates(section.getBound(), coordinates);
                        if (section.getRoads() != null) {
                            for (KakaoResponseDTO.Road road : section.getRoads()) {
                                addVertexesToCoordinates(road.getVertexes(), coordinates);
                            }
                        }
                        if (section.getGuides() != null) {
                            for (KakaoResponseDTO.Guide guide : section.getGuides()) {
                                coordinates.add(new Point(guide.getX(), guide.getY()));
                            }
                        }
                    }
                }
            }
        }

        return coordinates;
    }

    private void addLocationToCoordinates(KakaoResponseDTO.Location location, List<Point> coordinates) {
        if (location != null) {
            coordinates.add(new Point(location.getX(), location.getY()));
        }
    }

    private void addBoundToCoordinates(KakaoResponseDTO.Bound bound, List<Point> coordinates) {
        if (bound != null) {
            coordinates.add(new Point(bound.getMinX(), bound.getMinY()));
            coordinates.add(new Point(bound.getMaxX(), bound.getMaxY()));
        }
    }

    private void addVertexesToCoordinates(List<Double> vertexes, List<Point> coordinates) {
        if (vertexes != null && !vertexes.isEmpty()) {
            for (int i = 0; i < vertexes.size(); i += 2) {
                coordinates.add(new Point(vertexes.get(i), vertexes.get(i + 1)));
            }
        }
    }

    @Async
    public CompletableFuture<List<DamageAppResponseDTO>> checkCoordinates(List<Point> coordinates) {
        List<String> hexagonIndexes = coordinates.stream()
                .map(point -> h3Service.getH3Index(point.getY(), point.getX(), 13))
                .distinct()
                .collect(Collectors.toList());
        return CompletableFuture.supplyAsync(() -> damageRepository.findByHexagonIndexIn(hexagonIndexes));
    }
}
