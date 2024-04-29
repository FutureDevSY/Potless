package com.potless.backend.project.service;

import com.potless.backend.project.dto.request.TaskAddRequestDto;

public interface TaskService {
    Long addTaskToProject(TaskAddRequestDto taskAddRequestDto);
}
