package Potless.Backend.project.service;

import Potless.Backend.project.dto.request.ProjectListRequestDto;
import Potless.Backend.project.dto.request.ProjectSaveRequestDto;
import Potless.Backend.project.dto.response.ProjectDetailResponseDto;
import Potless.Backend.project.dto.response.ProjectListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    Page<ProjectListResponseDto> getProjectAll(ProjectListRequestDto projectListRequestDto, Pageable pageable);

    Long createProject(ProjectSaveRequestDto projectSaveDto);

    ProjectDetailResponseDto getProjectDetail(Long projectId);
}
