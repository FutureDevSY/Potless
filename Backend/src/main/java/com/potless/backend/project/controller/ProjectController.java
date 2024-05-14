package com.potless.backend.project.controller;

import com.potless.backend.global.exception.project.ProjectDeleteFailException;
import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.path.service.PathService;
import com.potless.backend.project.dto.request.ProjectDoneRequestDto;
import com.potless.backend.project.dto.request.ProjectListRequestDto;
import com.potless.backend.project.dto.request.ProjectSaveRequestDto;
import com.potless.backend.project.dto.response.ProjectDetailResponseDto;
import com.potless.backend.project.dto.response.ProjectListResponseDto;
import com.potless.backend.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/project")
@Tag(name = "Project 컨트롤러", description = "Project Controller API")
public class ProjectController {
    private final ProjectService projectService;
    private final PathService pathService;
    private final ApiResponse response;

    @Operation(summary = "프로젝트 리스트 조회", description = "프로젝트 리스트를 조회합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로젝트 리스트 조회 성공", content = @Content(schema = @Schema(implementation = ProjectListResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<?> getProjectAll(
            @Parameter(hidden = true) Authentication authentication,
            @ModelAttribute ProjectListRequestDto projectListRequestDto,
            @PageableDefault(size = 10) Pageable pageable,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        Page<ProjectListResponseDto> projectListResponseDtos = projectService.getProjectAll(authentication.getName(), projectListRequestDto, pageable);
        return response.success(ResponseCode.PROJECT_LIST_FETCHED, projectListResponseDtos);
    }


    @Operation(summary = "프로젝트 상세 조회", description = "단일 프로젝트를 조회합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "단일 프로젝트 조회 성공", content = @Content(schema = @Schema(implementation = ProjectDetailResponseDto.class)))
    })
    @GetMapping("{projectId}")
    public ResponseEntity<?> getProjectDetail(
            @PathVariable Long projectId
    ) {
        ProjectDetailResponseDto projectDetailResponseDto = projectService.getProjectDetail(projectId);
        return response.success(ResponseCode.PROJECT_FETCHED, projectDetailResponseDto);
    }

    @Operation(summary = "프로젝트 생성", description = "프로젝트를 생성합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로젝트 생성 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping
    public ResponseEntity<?> createProject(
            @Parameter(hidden = true) Authentication authentication,
            @RequestBody ProjectSaveRequestDto projectSaveRequestDto
    ) {
        int[] order;
        if (projectSaveRequestDto.getDamageNums() != null && !projectSaveRequestDto.getDamageNums().isEmpty()) {
            order = pathService.getOptimalOrder(projectSaveRequestDto.getOrigin(), projectSaveRequestDto.getDamageNums());
        } else order = new int[0];
        Long result = projectService.createProject(authentication.getName(), projectSaveRequestDto, order);
        return response.success(ResponseCode.PROJECT_DETECTED, result);
    }

    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로젝트 삭제 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("{projectId}")
    public ResponseEntity<?> deleteProject(
            @PathVariable Long projectId
    ) {
        projectService.deleteProject(projectId);
        return response.success(ResponseCode.PROJECT_DELETED);
    }

    @Operation(summary = "프로젝트 상태 변경", description = "프로젝트 작업 상태를 작업완료로 변경합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로젝트 상태 변경 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/workDone")
    public ResponseEntity<?> changeStatus(
            Authentication authentication,
            @RequestBody @Valid ProjectDoneRequestDto projectDoneRequestDto,
            BindingResult bindingResult) {
        projectService.changeProjectStatus(projectDoneRequestDto.getProjectId());
        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }
        return response.success(ResponseCode.PROJECT_DONE_WORK);
    }
}
