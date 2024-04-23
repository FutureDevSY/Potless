package Potless.Backend.project.service;

import Potless.Backend.project.dto.request.ProjectSaveRequestDto;

public interface ProjectService {
    Long createProject(ProjectSaveRequestDto projectSaveDto);
}
