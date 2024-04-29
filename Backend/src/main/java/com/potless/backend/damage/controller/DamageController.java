package com.potless.backend.damage.controller;


import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageVerificationRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.service.IDamageService;
import com.potless.backend.damage.service.IVerificationService;
import com.potless.backend.damage.service.KakaoService;
import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/damage")
@Tag(name = "DAMAGE 컨트롤러", description = "DAMAGE Controller API")
public class DamageController {

    private final IDamageService iDamageService;
    private final KakaoService kakaoService;
    private final ApiResponse response;
    private final IVerificationService iVerificationService;
    // 유효성 검사 오류 메시지를 처리해서 ApiResponse 객체를 반환하는 메서드



    @Operation(summary = "Damage 리스트 조회", description = "리스트 조회")
    @GetMapping
    public ResponseEntity<?> getDamages(
            @ModelAttribute DamageSearchRequestDTO damageSearchRequestDTO,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<DamageResponseDTO> damages = iDamageService.getDamages(damageSearchRequestDTO, pageable);
        return response.success(ResponseCode.POTHOLE_LIST_FETCHED, damages);
    }

    @Operation(summary = "Damage 조회", description = "조회")
    @GetMapping({"{damageId}"})
    public ResponseEntity<?> getDamage(@PathVariable Long damageId) {
        log.info("damageId = {}", damageId);
        DamageResponseDTO damageResponseDTO = iDamageService.getDamage(damageId);
        return response.success(ResponseCode.POTHOLE_FETCHED, damageResponseDTO);
    }

    @Operation(summary = "Damage 삽입", description = "삽입")
    @PostMapping("set")
    public ResponseEntity<?> setDamage(@RequestBody @Validated DamageSetRequestDTO damageSetRequestDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }
        // 위험도 파악 비동기

        // 비동기로 처리하고 바로 응답 반환 검증
        kakaoService.fetchKakaoData(damageSetRequestDTO.getX(), damageSetRequestDTO.getY())
                .thenAcceptAsync(data -> {
                    DamageVerificationRequestDTO verificationRequestDTO = DamageVerificationRequestDTO.builder()
                            .dtype(damageSetRequestDTO.getDtype())
                            .damageAddress(data.getDocuments().get(0).getRoad_address().getAddress_name())
                            .location(data.getDocuments().get(0).getAddress().getRegion_3depth_name())
                            .damageRoadName(data.getDocuments().get(0).getRoad_address().getRoad_name())
                            .area(data.getDocuments().get(0).getAddress().getRegion_2depth_name())
                            .build();

                    List<DamageResponseDTO> damageVerification = iDamageService.getDamageVerification(verificationRequestDTO);
                    log.info("damageVerification = {}", damageVerification);
                    if (iVerificationService.verificationDamage(damageVerification)) {
                        // width 구하기, 위험도 구하기 로직 추가 해야됨

                        DamageSetRequestServiceDTO serviceDTO = DamageSetRequestServiceDTO.builder()
                                .dirX(damageSetRequestDTO.getX())
                                .dirY(damageSetRequestDTO.getY())
                                .dtype(damageSetRequestDTO.getDtype())
                                .width(10.000)
                                .address(data.getDocuments().get(0).getRoad_address().getAddress_name())
                                .roadName(data.getDocuments().get(0).getRoad_address().getRoad_name())
                                .severity(1)
                                .status(Status.작업전)
                                .area(data.getDocuments().get(0).getAddress().getRegion_2depth_name())
                                .location(data.getDocuments().get(0).getAddress().getRegion_3depth_name())
                                .images(damageSetRequestDTO.getImages())
                                .build();

                        iDamageService.setDamage(serviceDTO);
                    }
                });


        return response.success(ResponseCode.POTHOLE_DETECTED);
    }
}
