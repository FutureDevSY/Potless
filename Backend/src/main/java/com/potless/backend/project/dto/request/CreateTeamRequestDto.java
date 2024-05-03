package com.potless.backend.project.dto.request;

import com.potless.backend.member.dto.WorkerInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CreateTeamRequestDto {

    private String teamName;

    private ArrayList<WorkerInfoDto> workerList;

    private String area;


}
