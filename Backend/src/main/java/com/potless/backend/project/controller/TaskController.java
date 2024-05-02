package com.potless.backend.project.controller;

import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.project.dto.request.TaskAddRequestDto;
import com.potless.backend.project.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/task")
@Tag(name = "Task 컨트롤러", description = "Task Controller API")
public class TaskController {
    private final TaskService taskService;
    private final ApiResponse response;

    @Operation(summary = "Project에 Task 입력")
    @PostMapping
    public ResponseEntity<?> addTaskToProject(@RequestBody TaskAddRequestDto taskAddRequestDto) {
        List<Long> result = taskService.addTaskToProject(taskAddRequestDto);
        return response.success(ResponseCode.PROJECT_FETCHED,result);
    }

}
