package com.potless.backend.project.controller;

import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.path.service.PathService;
import com.potless.backend.project.dto.request.TaskAddRequestDto;
import com.potless.backend.project.dto.request.TaskDeleteRequestDto;
import com.potless.backend.project.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/task")
@Tag(name = "Task 컨트롤러", description = "Task Controller API")
public class TaskController {
    private final TaskService taskService;
    private final PathService pathService;
    private final ApiResponse response;

    @Operation(summary = "프로젝트에 task 입력", description = "프로젝트에 task를 입력합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "task 입력 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping
    public ResponseEntity<?> addTaskToProject(@RequestBody TaskAddRequestDto taskAddRequestDto) {
        List<Long> result = taskService.addTaskToProject(taskAddRequestDto);
        pathService.updateOptimalOrder(result, taskAddRequestDto.getOrigin());
        return response.success(ResponseCode.TASK_DETECTED, result);
    }

    @Operation(summary = "프로젝트에서 task 삭제", description = "프로젝트에서 task를 삭제합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "task 삭제 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PatchMapping
    public ResponseEntity<?> deleteProject(
            @RequestBody TaskDeleteRequestDto taskDeleteRequestDto
    ){
        long taskId = taskDeleteRequestDto.getTaskId();
        long projectId = taskService.deleteTask(taskId);
        pathService.updateOptimalOrder(projectId, taskDeleteRequestDto.getOrigin());
        return response.success(ResponseCode.TASK_DELETED);
    }

    @Operation(summary = "작업자별 할당된 작업 목록 조회", description = "작업자에 할당된 작업 목록 조회")
    @GetMapping
    public ResponseEntity<?> getTask() {
        return response.success(ResponseCode.TASK_DETECTED, taskService.getTask());
    }

}
