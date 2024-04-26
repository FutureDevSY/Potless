package com.potless.backend.path.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class KakaoWaypointRequest {

    private Location origin;
    private Location destination;
    private List<Location> waypoints;
    private String[] avoid;

    @Builder
    public KakaoWaypointRequest(Location origin, List<Location> waypoints) {
        this.origin = origin;
        this.destination = origin;
        this.waypoints = waypoints;
        this.avoid = new String[] {"uturn"};
    }

}
