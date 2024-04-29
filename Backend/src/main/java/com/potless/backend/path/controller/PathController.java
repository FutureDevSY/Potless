package com.potless.backend.path.controller;

import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.path.dto.GetOptimalPathRequest;
import com.potless.backend.path.dto.KakaoWaypointResponse;
import com.potless.backend.path.service.PathService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/path")
@Tag(name = "Path 컨트롤러", description = "Path Controller API")
public class PathController {

    private final PathService pathService;
    private final ApiResponse response;

    @Operation(summary = "최적 경로 조회", description = "작업할 모든 포트홀을 지나는 최적 경로를 조회합니다", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "최적 경로 조회 성공", content = @Content(schema = @Schema(implementation = KakaoWaypointResponse.class)))
    })
    @PostMapping("/optimal")
    public ResponseEntity<?> getOptimalPath(@Valid @RequestBody GetOptimalPathRequest getOptimalPathRequest) {

        return response.success(ResponseCode.OPTIMAL_PATH_FOUND, pathService.getOptimalPath(getOptimalPathRequest));
    }

}