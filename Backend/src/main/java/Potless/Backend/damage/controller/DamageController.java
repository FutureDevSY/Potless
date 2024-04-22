package Potless.Backend.damage.controller;

import Potless.Backend.damage.dto.controller.request.DamageSearchRequestDTO;
import Potless.Backend.damage.dto.controller.request.DamageSetRequestDTO;
import Potless.Backend.damage.dto.controller.response.DamageResponseDTO;
import Potless.Backend.damage.service.IDamageService;
import Potless.Backend.damage.service.KakaoService;
import Potless.Backend.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/damage")
public class DamageController {

    private final IDamageService iDamageService;
    private final KakaoService kakaoService;

    // 유효성 검사 오류 메시지를 처리해서 ApiResponse 객체를 반환하는 메서드
    private ApiResponse<Object> getValidationErrorResponse(BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ObjectError error : result.getAllErrors()) {
                if (error instanceof FieldError fieldError) {
                    errorMessage.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append("; ");
                } else {
                    errorMessage.append(error.getDefaultMessage()).append("; ");
                }
            }
            log.info("Validation errors: {}", errorMessage);
            return ApiResponse.of(HttpStatus.BAD_REQUEST, errorMessage.toString(), null);
        }
        return null;
    }

    @GetMapping
    public ApiResponse<Object> getDamages(
            @ModelAttribute DamageSearchRequestDTO damageSearchRequestDTO,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<DamageResponseDTO> damages = iDamageService.getDamages(damageSearchRequestDTO, pageable);
        return ApiResponse.ok(damages);
    }

    @GetMapping({"{damageId}"})
    public ApiResponse<Object> getDamage(@PathVariable Long damageId) {
        log.info("damageId = {}", damageId);
        DamageResponseDTO damageResponseDTO = iDamageService.getDamage(damageId);
        return ApiResponse.ok(damageResponseDTO);
    }

    @PostMapping("set")
    public ApiResponse<Object> setDamage(@RequestBody @Validated DamageSetRequestDTO damageSetRequestDTO, BindingResult result) {
        ApiResponse<Object> validationResponse = getValidationErrorResponse(result);
        if (validationResponse != null)
            return validationResponse;

        // 비동기로 처리하고 바로 응답 반환
        kakaoService.fetchKakaoData(damageSetRequestDTO.getX(), damageSetRequestDTO.getY())
                .thenAcceptAsync(data -> {
                    // 로그로 데이터를 출력
                    log.info("Kakao API Response: {}", data);

                    // 데이터를 처리하고 DB에 저장하는 로직을 여기에 추가
                    iDamageService.setDamage();
                });


        return ApiResponse.ok("Request is being processed.");
    }
}
