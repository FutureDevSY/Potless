package com.potless.backend.project.service;

import com.potless.backend.project.dto.request.*;
import com.potless.backend.project.dto.response.ProjectDetailResponseDto;
import com.potless.backend.project.dto.response.ProjectListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ProjectService {

    Page<ProjectListResponseDto> getProjectAll(String email, ProjectListRequestDto projectListRequestDto, Pageable pageable);

    Long createProject(String email, ProjectSaveRequestDto projectSaveDto, int[] order);

    ProjectDetailResponseDto getProjectDetail(Long projectId);

    void deleteProject(Long projectId);



}
