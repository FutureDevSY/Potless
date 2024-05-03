package com.potless.backend.path.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "최적 경로 조회 응답 DTO")
public class KakaoWaypointResponse {

    @Schema(description = "결과 ID")
    private String transId;
    @Schema(description = "경로 정보")
    private List<Route> routes;

    @Getter
    @Builder
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Route {
        @Schema(description = "경로 탐색 결과 코드")
        private Integer resultCode;
        @Schema(description = "경로 탐색 결과 메시지")
        private String resultMsg;
        @Schema(description = "경로 요약 정보")
        private Summary summary;
        @Schema(description = "구간별 경로 정보")
        private List<Section> sections;
    }

    @Getter
    @Builder
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Summary {
        @Schema(description = "출발지 정보")
        private Location origin;
        @Schema(description = "도착지 정보")
        private Location destination;
        @Schema(description = "경유지 정보")
        private List<Location> waypoints;
        @Schema(description = "경로 탐색 우선순위 옵션")
        private String priority;
        @Schema(description = "모든 경로를 포함하는 사각형의 바운딩 박스")
        private Bound bound;
        @Schema(description = "요금 정보")
        private Fare fare;
        @Schema(description = "전체 검색 결과 거리(미터)")
        private Integer distance;
        @Schema(description = "목적지까지 소요 시간(초)")
        private Integer duration;

        @Getter
        @Builder
        public static class Fare {
            @Schema(description = "택시 요금(원)")
            private Integer taxi;
            @Schema(description = "통행 요금(원)")
            private Integer toll;
        }
    }

    @Getter
    @Builder
    public static class Section {
        @Schema(description = "섹션 거리(미터)")
        private Integer distance;
        @Schema(description = "섹션 이동 시간(초)")
        private Integer duration;
        @Schema(description = "섹션 경로를 포함하는 사각형의 바운딩 박스")
        private Bound bound;
        @Schema(description = "도로 정보")
        private List<Road> roads;
        @Schema(description = "안내 정보")
        private List<Guide> guides;

        @Getter
        @Builder
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Road {
            @Schema(description = "도로명")
            private String name;
            @Schema(description = "도로 길이(미터)")
            private Integer distance;
            @Schema(description = "예상 이동 시간(초)")
            private Integer duration;
            @Schema(description = "현재 교통 정보 속도(km/h)")
            private Double trafficSpeed;
            @Schema(description = "현재 교통 정보 상태")
            private Integer trafficState;
            @Schema(description = "X, Y 좌표로 구성된 1차원 배열")
            private List<Double> vertexes;
        }

        @Getter
        @Builder
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Guide {
            @Schema(description = "명칭")
            private String name;
            @Schema(description = "X 좌표(경도)")
            private Double x;
            @Schema(description = "Y 좌표(위도)")
            private Double y;
            @Schema(description = "이전 가이드 지점부터 현재 가이드 지점까지 거리(미터)")
            private Integer distance;
            @Schema(description = "이전 가이드 지점부터 현재 가이드 지점까지 시간(초)")
            private Integer duration;
            @Schema(description = "안내 타입")
            private Integer type;
            @Schema(description = "안내 문구")
            private String guidance;
            @Schema(description = "현재 가이드에 대한 링크 인덱스")
            private Integer roadIndex;
        }
    }

    @Getter
    @Builder
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Bound {
        @Schema(description = "바운딩 박스 왼쪽 하단의 X 좌표")
        private Double minX;
        @Schema(description = "바운딩 박스 왼쪽 하단의 Y 좌표")
        private Double minY;
        @Schema(description = "바운딩 박스 오른쪽 상단의 X 좌표")
        private Double maxX;
        @Schema(description = "바운딩 박스 오른쪽 상단의 Y 좌표")
        private Double maxY;
    }

}
