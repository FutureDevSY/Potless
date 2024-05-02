package com.potless.backend.project.service;

import com.potless.backend.project.dto.request.TaskAddRequestDto;

import java.util.List;

public interface TaskService {
    List<Long> addTaskToProject(TaskAddRequestDto taskAddRequestDto);

    void deleteTask(Long taskId);
}
