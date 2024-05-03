package com.potless.backend.project.service;

import com.potless.backend.project.dto.request.TaskAddRequestDto;
import com.potless.backend.project.dto.response.GetTaskResponseDto;

import java.util.List;

public interface TaskService {
    List<Long> addTaskToProject(TaskAddRequestDto taskAddRequestDto);
    Long deleteTask(Long taskId);
    GetTaskResponseDto getTask();

}
