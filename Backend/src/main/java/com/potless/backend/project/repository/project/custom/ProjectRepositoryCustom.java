package com.potless.backend.project.repository.project.custom;

import com.potless.backend.project.dto.request.ProjectListRequestDto;
import com.potless.backend.project.dto.response.ProjectDetailResponseDto;
import com.potless.backend.project.dto.response.ProjectListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryCustom {
    Page<ProjectListResponseDto> findProjectAll(Long managerId, ProjectListRequestDto projectListRequestDto, Pageable pageable);

    ProjectDetailResponseDto getProjectDetail(Long projectId);

}