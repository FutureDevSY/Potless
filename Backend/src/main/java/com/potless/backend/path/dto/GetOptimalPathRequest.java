package com.potless.backend.path.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetOptimalPathRequest {

    private Location origin;
    private List<Location> potholeList;

}
