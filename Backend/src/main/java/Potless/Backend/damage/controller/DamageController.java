package Potless.Backend.damage.controller;

import Potless.Backend.global.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("road")
public class DamageController {

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
    public ApiResponse<Object> getDamages() {


        return ApiResponse.ok(null);
    }
}
