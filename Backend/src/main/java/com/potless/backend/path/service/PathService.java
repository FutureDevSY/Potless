package com.potless.backend.path.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.global.exception.project.ProjectNotFoundException;
import com.potless.backend.global.exception.task.TaskNotFoundException;
import com.potless.backend.path.dto.*;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.entity.TaskEntity;
import com.potless.backend.project.repository.project.ProjectRepository;
import com.potless.backend.project.repository.task.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@Component
@Transactional
@RequiredArgsConstructor
public class PathService {

    private static final int INF = 16_000_000;
    private static int[] path;
    private static int[][] distance, memoization, previousNode;
    private final String DISTANCE_URL = "https://apis-navi.kakaomobility.com/v1/directions?priority=DISTANCE&avoid=uturn";
    private final String WAYPOINT_URL = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";

    private final TaskRepository taskRepository;
    private final DamageRepository damageRepository;

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

    public KakaoWaypointResponse getOptimalPath(GetOptimalPathRequestDto getOptimalPathRequestDto) {
        List<TaskEntity> taskEntityList = taskRepository.findTasksByProjectId(getOptimalPathRequestDto.getProjectId());
        List<Location> taskList = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntityList) {
            taskList.add(Location.builder()
                    .x(taskEntity.getDamageEntity().getDirX())
                    .y(taskEntity.getDamageEntity().getDirY())
                    .build());
        }
        KakaoWaypointRequest kakaoWaypointRequest = KakaoWaypointRequest.builder()
                .origin(getOptimalPathRequestDto.getOrigin())
                .waypoints(taskList)
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

    public int[] getOptimalOrder(Location origin, List<Long> damageIdList) {
        List<Location> potholeList = new ArrayList<>();
        damageIdList.forEach(damageId -> {
            DamageEntity damageEntity = damageRepository.findById(damageId)
                    .orElseThrow(PotholeNotFoundException::new);
            potholeList.add(Location.builder()
                    .x(damageEntity.getDirX())
                    .y(damageEntity.getDirY())
                    .build());
        });

        int nodeNum = potholeList.size() + 1;
        int subsetNum = 1 << (nodeNum - 1);
        getDistance(origin, potholeList, nodeNum, subsetNum);
        tspAlgorithm(nodeNum, subsetNum);
        findOptimalPath(nodeNum, subsetNum);

        return path;
    }

    public void updateOptimalOrder(Long projectId, Location origin) {
        List<TaskEntity> taskEntityList = taskRepository.findTasksByProjectId(projectId);
        if (!taskEntityList.isEmpty()) updateOrder(origin, taskEntityList);
    }

    public void updateOrder(Location origin, List<TaskEntity> taskEntityList) {
        List<Location> potholeList = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntityList) {
            DamageEntity damageEntity = taskEntity.getDamageEntity();
            potholeList.add(Location.builder()
                    .x(damageEntity.getDirX())
                    .y(damageEntity.getDirY())
                    .build());
        }

        int nodeNum = potholeList.size() + 1;
        int subsetNum = 1 << (nodeNum - 1);
        getDistance(origin, potholeList, nodeNum, subsetNum);
        tspAlgorithm(nodeNum, subsetNum);
        findOptimalPath(nodeNum, subsetNum);

        for (int i = 1; i < nodeNum; i++) {
            TaskEntity taskEntity = taskEntityList.get(path[i] - 1);
            taskEntity.changeTaskOrder(i);
        }
    }

    public void getDistance(Location origin, List<Location> potholeList, int nodeNum, int subsetNum) {
        distance = new int[nodeNum][nodeNum];
        memoization = new int[nodeNum][subsetNum];
        previousNode = new int[nodeNum][subsetNum];
        path = new int[nodeNum + 1];

        for (int i = 0; i < nodeNum - 1; i++) {
            if (i != 0) origin = potholeList.get(i - 1);
            for (int j = i + 1; j < nodeNum; j++) {
                Location destination = potholeList.get(j - 1);

                WebClient webClient = getBaseUrl(DISTANCE_URL + "&origin=" + origin.getX() + "," + origin.getY()
                        + "&destination=" + destination.getX() + "," + destination.getY());
                Mono<KakaoDistanceResponse> response = webClient.get()
                        .retrieve()
                        .bodyToMono(KakaoDistanceResponse.class).log();
                KakaoDistanceResponse kakaoDistanceResponse = response.block();
                if (kakaoDistanceResponse.getRoutes().get(0).getResultCode() != 0) continue;
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
