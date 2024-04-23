package Potless.Backend.damage.controller;

import Potless.Backend.damage.dto.controller.request.DamageSearchRequestDTO;
import Potless.Backend.damage.dto.controller.request.DamageSetRequestDTO;
import Potless.Backend.damage.dto.controller.response.DamageResponseDTO;
import Potless.Backend.damage.service.IDamageService;
import Potless.Backend.damage.service.KakaoService;
import Potless.Backend.global.format.code.ApiResponse;
import Potless.Backend.global.format.response.ResponseCode;
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
@RequestMapping("api/damage")
public class DamageController {

    private final IDamageService iDamageService;
    private final KakaoService kakaoService;
    private final ApiResponse response;
    // 유효성 검사 오류 메시지를 처리해서 ApiResponse 객체를 반환하는 메서드


    @GetMapping
    public ResponseEntity<?> getDamages(
            @ModelAttribute DamageSearchRequestDTO damageSearchRequestDTO,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<DamageResponseDTO> damages = iDamageService.getDamages(damageSearchRequestDTO, pageable);
        return response.success(ResponseCode.POTHOLE_LIST_FETCHED, damages);
    }

    @GetMapping({"{damageId}"})
    public ResponseEntity<?> getDamage(@PathVariable Long damageId) {
        log.info("damageId = {}", damageId);
        DamageResponseDTO damageResponseDTO = iDamageService.getDamage(damageId);
        return response.success(ResponseCode.POTHOLE_FETCHED, damageResponseDTO);
    }

    @PostMapping("set")
    public ResponseEntity<?> setDamage(@RequestBody @Validated DamageSetRequestDTO damageSetRequestDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }
        // 위험도 파악 비동기

        // 비동기로 처리하고 바로 응답 반환
        kakaoService.fetchKakaoData(damageSetRequestDTO.getX(), damageSetRequestDTO.getY())
                .thenAcceptAsync(data -> {
                    iDamageService.setDamage(data);
                });


        return response.success(ResponseCode.POTHOLE_DETECTED);
    }
}
