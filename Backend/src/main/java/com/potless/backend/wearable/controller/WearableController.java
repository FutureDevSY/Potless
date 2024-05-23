package com.potless.backend.wearable.controller;

import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.hexagon.service.HexagonService;
import com.potless.backend.wearable.dto.request.WearableRequestDTO;
import com.potless.backend.wearable.service.WearableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/wearable")
@Tag(name = "Wearable 컨트롤러", description = "wearable Controller API")
public class WearableController {

    private final WearableService wearableService;
    private final HexagonService hexagonService;
    private final ApiResponse response;
    @Operation(summary = "위도 경도 Damage 테이블 조회", description = "위도 경도 Damage 테이블 조회", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @GetMapping
    public ResponseEntity<?> createProject(
            @RequestBody WearableRequestDTO wearableRequestDTO
    ) {
        String h3Index = hexagonService.getH3Index(wearableRequestDTO.getY(), wearableRequestDTO.getX(), 13);
        boolean isCheck = wearableService.getDamage(h3Index);
        return response.success(ResponseCode.PROJECT_DETECTED, isCheck);
    }
}
