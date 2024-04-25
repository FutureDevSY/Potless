package Potless.Backend.project.controller;

import Potless.Backend.global.format.code.ApiResponse;
import Potless.Backend.global.format.response.ResponseCode;
import Potless.Backend.project.dto.request.ProjectListRequestDto;
import Potless.Backend.project.dto.request.ProjectSaveRequestDto;
import Potless.Backend.project.dto.response.ProjectDetailResponseDto;
import Potless.Backend.project.dto.response.ProjectListResponseDto;
import Potless.Backend.project.service.ProjectService;
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
    private ProjectService projectService;
    private ApiResponse response;

    @GetMapping
    public ResponseEntity<?> getProjectAll(
            @ModelAttribute ProjectListRequestDto projectListRequestDto,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<ProjectListResponseDto> projectListResponseDtos = projectService.getProjectAll(projectListRequestDto, pageable);
        return response.success(ResponseCode.PROJECT_LIST_FETCHED, projectListResponseDtos);
    }


    @GetMapping("{projectId}")
    public ResponseEntity<?> getProjectDetail(@PathVariable Long projectId) {
        log.info("projectId = {}", projectId);
        ProjectDetailResponseDto projectDetailResponseDto = projectService.getProjectDetail(projectId);
        return response.success(ResponseCode.PROJECT_FETCHED,projectDetailResponseDto);
    }

//    @PostMapping
//    public ResponseEntity<?> createProject(@RequestBody @Validated ProjectSaveRequestDto projectSaveRequestDto){
//
//        return
//    }
}
