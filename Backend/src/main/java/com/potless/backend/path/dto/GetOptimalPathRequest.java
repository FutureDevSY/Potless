package com.potless.backend.path.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetOptimalPathRequest {

    private Location origin;
    private List<Location> potholeList;

}
