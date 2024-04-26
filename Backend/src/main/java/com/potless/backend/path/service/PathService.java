package com.potless.backend.path.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potless.backend.path.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class PathService {


    private static final int INF = 16_000_000;
    private static int[] path;
    private static int[][] distance, memoization, previousNode;
    private final String DISTANCE_URL = "https://apis-navi.kakaomobility.com/v1/directions?priority=DISTANCE&avoid=uturn";
    private final String WAYPOINT_URL = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";
    @Value("${kakao.api-service-key}")
    private String KAKAO_API_KEY;

    public WebClient getBaseUrl(final String url) {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + KAKAO_API_KEY)
                .build()
                .mutate()
                .build();
    }

    public KakaoWaypointResponse getOptimalPath(GetOptimalPathRequest getOptimalPathRequest) {
        int nodeNum = getOptimalPathRequest.getPotholeList().size() + 1;
        int subsetNum = 1 << (nodeNum - 1);
        getDistance(getOptimalPathRequest, nodeNum, subsetNum);
        tspAlgorithm(nodeNum, subsetNum);
        findOptimalPath(nodeNum, subsetNum);

        List<Location> optimalPath = new ArrayList<>();
        for (int i = 1; i < nodeNum; i++) {
            optimalPath.add(getOptimalPathRequest.getPotholeList().get(i - 1));
        }
        KakaoWaypointRequest kakaoWaypointRequest = KakaoWaypointRequest.builder()
                .origin(getOptimalPathRequest.getOrigin())
                .waypoints(optimalPath)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        WebClient webClient = getBaseUrl(WAYPOINT_URL);
        try {
            Mono<KakaoWaypointResponse> response = webClient.post()
                    .body(BodyInserters.fromValue(objectMapper.writeValueAsString(kakaoWaypointRequest)))
                    .retrieve()
                    .bodyToMono(KakaoWaypointResponse.class).log();
            return response.block();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getDistance(GetOptimalPathRequest getOptimalPathRequest, int nodeNum, int subsetNum) {
        distance = new int[nodeNum][nodeNum];
        memoization = new int[nodeNum][subsetNum];
        previousNode = new int[nodeNum][subsetNum];
        path = new int[nodeNum + 1];

        for (int i = 0; i < nodeNum - 1; i++) {
            Location origin = getOptimalPathRequest.getOrigin();
            if (i != 0) origin = getOptimalPathRequest.getPotholeList().get(i - 1);
            for (int j = i + 1; j < nodeNum; j++) {
                Location destination = getOptimalPathRequest.getPotholeList().get(j - 1);

                WebClient webClient = getBaseUrl(DISTANCE_URL + "&origin=" + origin.getX() + "," + origin.getY()
                        + "&destination=" + destination.getX() + "," + destination.getY());
                Mono<KakaoDistanceResponse> response = webClient.get()
                        .retrieve()
                        .bodyToMono(KakaoDistanceResponse.class).log();
                KakaoDistanceResponse kakaoDistanceResponse = response.block();
                int dist = kakaoDistanceResponse.getRoutes().get(0).getSummary().getDistance();
                distance[i][j] = dist;
                distance[j][i] = dist;
            }
        }
    }

    public void tspAlgorithm(int nodeNum, int subsetNum) {
        for (int i = 1; i < nodeNum; i++) memoization[i][0] = distance[i][0];
        for (int n = 1; n < nodeNum; n++) {
            for (int A = 1; A < subsetNum - 1; A++) {
                if (count(A, nodeNum) == n) {
                    for (int i = 0; i < nodeNum - 1; i++) {
                        if ((A & (1 << i)) == 0) minimum(i + 1, A, nodeNum);
                    }
                }
            }
        }
        minimum(0, subsetNum - 1, nodeNum);
    }

    public int count(int A, int nodeNum) {
        int count = 0;
        for (int i = 0; i < nodeNum; i++) {
            if ((A & (1 << i)) != 0) count++;
        }
        return count;
    }

    public void minimum(int i, int A, int nodeNum) {
        int minValue = INF;
        int minIndex = 0;
        for (int j = 0; j < nodeNum - 1; j++) {
            if ((A & (1 << j)) != 0) {
                if (distance[i][j + 1] == 0) continue;
                int m = distance[i][j + 1] + memoization[j + 1][diff(A, j)];
                if (minValue > m) {
                    minValue = m;
                    minIndex = j + 1;
                }
            }
        }
        memoization[i][A] = minValue;
        previousNode[i][A] = minIndex;
    }

    public int diff(int A, int j) {
        int t = 1 << j;
        return (A & (~t));
    }

    public void findOptimalPath(int nodeNum, int subsetNum) {
        int A = subsetNum - 1;
        int curNode = 0;
        for (int i = 1; i <= nodeNum; i++) {
            curNode = previousNode[curNode][A];
            A = diff(A, curNode - 1);
            path[i] = curNode;
        }
    }

}
