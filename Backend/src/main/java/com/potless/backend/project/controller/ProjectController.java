package com.potless.backend.project.controller;

import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.project.dto.request.ProjectListRequestDto;
import com.potless.backend.project.dto.request.ProjectSaveRequestDto;
import com.potless.backend.project.dto.response.ProjectDetailResponseDto;
import com.potless.backend.project.dto.response.ProjectListResponseDto;
import com.potless.backend.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/project")
public class ProjectController {
    private final ProjectService projectService;
    private final ApiResponse response;


    @GetMapping
    public ResponseEntity<?> getProjectAll(
            @ModelAttribute ProjectListRequestDto projectListRequestDto,
            @PageableDefault(size = 10) Pageable pageable,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }
        Page<ProjectListResponseDto> projectListResponseDtos = projectService.getProjectAll(projectListRequestDto, pageable);
        return response.success(ResponseCode.PROJECT_LIST_FETCHED, projectListResponseDtos);
    }


//    @GetMapping("{projectId}")
//    public ResponseEntity<?> getProjectDetail(
//            @PathVariable Long projectId
//    ) {
//        log.info("projectId = {}", projectId);
//        ProjectDetailResponseDto projectDetailResponseDto = projectService.getProjectDetail(projectId);
//        return response.success(ResponseCode.PROJECT_FETCHED, projectDetailResponseDto);
//    }

    @PostMapping
    public ResponseEntity<?> createProject(
            @RequestBody ProjectSaveRequestDto projectSaveRequestDto
    ){
        System.out.println("projectSaveRequestDto = " + projectSaveRequestDto);
        Long result = projectService.createProject(projectSaveRequestDto);
        return response.success(ResponseCode.PROJECT_FETCHED, result);
    }

    @PutMapping("{projectId}")
    public ResponseEntity<?> deleteProject(
            @PathVariable Long projectId
    ){
        projectService.deleteProject(projectId);
        return response.success(ResponseCode.PROJECT_DELETED);
    }
}
