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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/wearable")
@Tag(name = "Wearable 컨트롤러", description = "wearable Controller API")
public class WearableController {

    private final WearableService wearableService;
    private final HexagonService hexagonService;
    private final ApiResponse response;

    @Operation(summary = "위도 경도 Damage 테이블 조회", description = "위도 경도 Damage 테이블 조회", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = WearableRequestDTO.class)))
    })
    @GetMapping
    public ResponseEntity<?> getDamage(
            @RequestParam("x") Double x,
            @RequestParam("y") Double y
    ) {
        String h3Index = hexagonService.getH3Index(y, x, 13);
        log.info("h3Index = {}", h3Index);
        boolean isCheck = wearableService.getDamage(h3Index);
        return response.success(ResponseCode.WEARABLE_FETCHED, isCheck);
    }
}
