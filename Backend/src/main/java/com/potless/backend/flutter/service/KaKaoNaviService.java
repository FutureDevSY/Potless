package com.potless.backend.flutter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.flutter.dto.service.response.DamageAppResponseDTO;
import com.potless.backend.flutter.dto.service.response.KakaoResponseDTO;
import com.potless.backend.flutter.dto.service.response.Point;
import com.potless.backend.hexagon.service.HexagonService;
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
import java.util.concurrent.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.flutter.dto.service.response.DamageAppResponseDTO;
import com.potless.backend.flutter.dto.service.response.KakaoResponseDTO;
import com.potless.backend.flutter.dto.service.response.Point;
import com.potless.backend.hexagon.service.HexagonService;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.flutter.dto.service.response.DamageAppResponseDTO;
import com.potless.backend.flutter.dto.service.response.KakaoResponseDTO;
import com.potless.backend.flutter.dto.service.response.Point;
import com.potless.backend.hexagon.service.HexagonService;
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
    private final HexagonService hexagonService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // 동시 실행 스레드 수 제한

    @Value("${kakao.api-service-key}")
    private String KAKAO_API_KEY;

    @Async
    public CompletableFuture<String> fetchKakaoData(Double startX, Double startY, Double endX, Double endY) {
        // 생략된 코드...
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
        }, executorService);
    }


    public List<Point> extractCoordinates(String kakaoResponse) throws Exception {
        KakaoResponseDTO kakaoResponseDTO = objectMapper.readValue(kakaoResponse, KakaoResponseDTO.class);
        List<Point> coordinates = new ArrayList<>();

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

        // 세분화된 좌표 리스트 생성
        List<Point> detailedCoordinates = new ArrayList<>();
        for (int i = 0; i < coordinates.size() - 1; i++) {
            Point start = coordinates.get(i);
            Point end = coordinates.get(i + 1);
            detailedCoordinates.add(start);
            detailedCoordinates.addAll(interpolate(start, end, 0.005));
        }
        detailedCoordinates.add(coordinates.get(coordinates.size() - 1));

        return detailedCoordinates;
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

    // 두 좌표 간 거리 계산
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double p = 0.017453292519943295; // Pi/180
        double a = 0.5 - Math.cos((lat2 - lat1) * p)/2 +
                Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                        (1 - Math.cos((lon2 - lon1) * p))/2;
        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
    }

    // 두 좌표 간 보간
    private List<Point> interpolate(Point start, Point end, double interval) {
        List<Point> interpolated = new ArrayList<>();

        double distance = calculateDistance(start.getY(), start.getX(), end.getY(), end.getX());
        int numPoints = (int) Math.floor(distance / interval);

        for (int j = 1; j <= numPoints; j++) {
            double lat = start.getY() + (end.getY() - start.getY()) * j / (numPoints + 1);
            double lon = start.getX() + (end.getX() - start.getX()) * j / (numPoints + 1);
            interpolated.add(new Point(lon, lat));
        }

        return interpolated;
    }

    @Async
    public CompletableFuture<List<DamageAppResponseDTO>> checkCoordinates(List<Point> coordinates) {
        List<String> hexagonIndexes = coordinates.stream()
                .map(point -> hexagonService.getH3Index(point.getY(), point.getX(), 13))
                .distinct()
                .collect(Collectors.toList());

        return CompletableFuture.supplyAsync(() -> damageRepository.findByHexagonIndexIn(hexagonIndexes), executorService);
    }
}
