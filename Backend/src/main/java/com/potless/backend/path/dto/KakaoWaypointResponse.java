package com.potless.backend.path.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoWaypointResponse {

    private String transId;
    private List<Route> routes;

    @Getter
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Route {
        private Integer resultCode;
        private String resultMsg;
        private Summary summary;
        private List<Section> sections;
    }

    @Getter
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Summary {
        private Location origin;
        private Location destination;
        private List<Location> waypoints;
        private String priority;
        private Bound bound;
        private Fare fare;
        private Integer distance;
        private Integer duration;

        @Getter
        public static class Fare {
            private Integer taxi;
            private Integer toll;
        }
    }

    @Getter
    public static class Section {
        private Integer distance;
        private Integer duration;
        private Bound bound;
        private List<Road> roads;
        private List<Guide> guides;

        @Getter
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Road {
            private String name;
            private Integer distance;
            private Integer duration;
            private Double trafficSpeed;
            private Integer trafficState;
            private List<Double> vertexes;
        }

        @Getter
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Guide {
            private String name;
            private Double x;
            private Double y;
            private Integer distance;
            private Integer duration;
            private Integer type;
            private String guidance;
            private Integer roadIndex;
        }
    }

    @Getter
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Bound {
        private Double minX;
        private Double minY;
        private Double maxX;
        private Double maxY;
    }

}
