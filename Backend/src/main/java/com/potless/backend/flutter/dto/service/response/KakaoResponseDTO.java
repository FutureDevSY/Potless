package com.potless.backend.flutter.dto.service.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponseDTO {

    @JsonProperty("trans_id")
    private String transId;

    @JsonProperty("routes")
    private List<Route> routes;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        @JsonProperty("result_code")
        private int resultCode;

        @JsonProperty("result_msg")
        private String resultMsg;

        @JsonProperty("summary")
        private Summary summary;

        @JsonProperty("sections")
        private List<Section> sections;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Summary {
        @JsonProperty("origin")
        private Location origin;

        @JsonProperty("destination")
        private Location destination;

        @JsonProperty("waypoints")
        private List<Location> waypoints;

        @JsonProperty("priority")
        private String priority;

        @JsonProperty("bound")
        private Bound bound;

        @JsonProperty("fare")
        private Fare fare;

        @JsonProperty("distance")
        private int distance;

        @JsonProperty("duration")
        private int duration;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        @JsonProperty("name")
        private String name;

        @JsonProperty("x")
        private double x;

        @JsonProperty("y")
        private double y;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bound {
        @JsonProperty("min_x")
        private double minX;

        @JsonProperty("min_y")
        private double minY;

        @JsonProperty("max_x")
        private double maxX;

        @JsonProperty("max_y")
        private double maxY;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fare {
        @JsonProperty("taxi")
        private int taxi;

        @JsonProperty("toll")
        private int toll;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Section {
        @JsonProperty("distance")
        private int distance;

        @JsonProperty("duration")
        private int duration;

        @JsonProperty("bound")
        private Bound bound;

        @JsonProperty("roads")
        private List<Road> roads;

        @JsonProperty("guides")
        private List<Guide> guides;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Road {
        @JsonProperty("name")
        private String name;

        @JsonProperty("distance")
        private int distance;

        @JsonProperty("duration")
        private int duration;

        @JsonProperty("traffic_speed")
        private double trafficSpeed;

        @JsonProperty("traffic_state")
        private int trafficState;

        @JsonProperty("vertexes")
        private List<Double> vertexes;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Guide {
        @JsonProperty("name")
        private String name;

        @JsonProperty("x")
        private double x;

        @JsonProperty("y")
        private double y;

        @JsonProperty("distance")
        private int distance;

        @JsonProperty("duration")
        private int duration;

        @JsonProperty("type")
        private int type;

        @JsonProperty("guidance")
        private String guidance;

        @JsonProperty("road_index")
        private int roadIndex;
    }
}
