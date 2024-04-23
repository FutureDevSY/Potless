package Potless.Backend.project.repository.project.custom;

import Potless.Backend.project.dto.request.ProjectListRequestDto;
import Potless.Backend.project.dto.request.ProjectSaveRequestDto;
import Potless.Backend.project.dto.response.ProjectListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryCustom {
    Page<ProjectListResponseDto> findProjectAllByManagerId(ProjectListRequestDto projectListRequestDto, Pageable pageable);
}