package com.potless.backend.project.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CreateTeamRequestDto {

    private String teamName;

    private ArrayList<String> memberNameList;

     private Long areaId;


}
