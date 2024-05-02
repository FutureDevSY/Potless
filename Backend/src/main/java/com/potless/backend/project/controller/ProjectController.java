package com.potless.backend.project.controller;

import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.path.service.PathService;
import com.potless.backend.project.dto.request.WorkerRequestDto;
import com.potless.backend.project.dto.request.CreateTeamRequestDto;
import com.potless.backend.project.dto.request.ProjectListRequestDto;
import com.potless.backend.project.dto.request.ProjectSaveRequestDto;
import com.potless.backend.project.dto.response.ProjectDetailResponseDto;
import com.potless.backend.project.dto.response.ProjectListResponseDto;
import com.potless.backend.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/project")
@Tag(name = "Project 컨트롤러", description = "Project Controller API")
public class ProjectController {
    private final ProjectService projectService;
    private final PathService pathService;
    private final ApiResponse response;


    @Operation(summary = "Project 리스트 조회")
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


    @Operation(summary = "Project 상세조회")
    @GetMapping("{projectId}")
    public ResponseEntity<?> getProjectDetail(
            @PathVariable Long projectId
    ) {
        log.info("projectId = {}", projectId);
        ProjectDetailResponseDto projectDetailResponseDto = projectService.getProjectDetail(projectId);
        return response.success(ResponseCode.PROJECT_FETCHED, projectDetailResponseDto);
    }

    @Operation(summary = "Project 생성")
    @PostMapping
    public ResponseEntity<?> createProject(
            @Parameter(hidden = true) Authentication authentication,
            @RequestBody ProjectSaveRequestDto projectSaveRequestDto
    ){
        System.out.println("projectSaveRequestDto = " + projectSaveRequestDto);
        int[] order = pathService.getOptimalOrder(projectSaveRequestDto.getOrigin(), projectSaveRequestDto.getDamageNums());
        Long result = projectService.createProject(authentication.getName(), projectSaveRequestDto, order);
        return response.success(ResponseCode.PROJECT_DETECTED, result);
    }

    @Operation(summary = "Project 삭제")
    @DeleteMapping("{projectId}")
    public ResponseEntity<?> deleteProject(
            @PathVariable Long projectId
    ){
        projectService.deleteProject(projectId);
        return response.success(ResponseCode.PROJECT_DELETED);
    }
}
