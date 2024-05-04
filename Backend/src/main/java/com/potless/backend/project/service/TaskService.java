package com.potless.backend.project.service;

import com.potless.backend.project.dto.request.TaskAddRequestDto;
import com.potless.backend.project.dto.response.GetTaskResponseDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TaskService {
    List<Long> addTaskToProject(TaskAddRequestDto taskAddRequestDto);
    Long deleteTask(Long taskId);
    List<GetTaskResponseDto> getTaskList(Authentication authentication);

}
