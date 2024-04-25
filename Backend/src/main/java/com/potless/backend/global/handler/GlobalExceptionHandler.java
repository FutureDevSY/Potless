package com.potless.backend.global.handler;

import com.potless.backend.global.exception.email.FailedMessageTransmissionException;
import com.potless.backend.global.exception.email.InvalidAuthCodeException;
import com.potless.backend.global.exception.member.DuplicateEmailException;
import com.potless.backend.global.exception.member.MissingPathVariableException;
import com.potless.backend.global.exception.member.PasswordMismatchException;
import com.potless.backend.global.exception.pothole.PotholeAreaNotFoundException;
import com.potless.backend.global.exception.pothole.PotholeLocationNotFoundException;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.global.exception.member.InvalidLoginAttemptException;

import com.potless.backend.global.exception.member.EmailNotFoundException;
import com.potless.backend.global.exception.jwt.RefreshTokenNotFoundException;
import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ApiResponse response;

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handle(Exception e) {
        log.error("Exception = {}", e.getMessage());
        e.printStackTrace();
        return response.error(ErrorCode.GLOBAL_UNEXPECTED_ERROR);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    protected ResponseEntity<?> handle(PasswordMismatchException e) {
        log.error("PasswordMismatchException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    protected ResponseEntity<?> handle(DuplicateEmailException e) {
        log.error("DuplicateEmailException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }

    @ExceptionHandler(FailedMessageTransmissionException.class)
    protected ResponseEntity<?> handle(FailedMessageTransmissionException e) {
        log.error("FailedMessageTransmissionException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }

    @ExceptionHandler(InvalidAuthCodeException.class)
    protected ResponseEntity<?> handle(InvalidAuthCodeException e) {
        log.error("InvalidAuthCodeException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }

    @ExceptionHandler(InvalidLoginAttemptException.class)
    protected ResponseEntity<?> handle(InvalidLoginAttemptException e) {
        log.error("InvalidLoginAttemptException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    protected ResponseEntity<?> handle(EmailNotFoundException e) {
        log.error("EmailNotFoundException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    protected ResponseEntity<?> handle(RefreshTokenNotFoundException e) {
        log.error("RefreshTokenNotFoundException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }


    @ExceptionHandler(MissingPathVariableException.class)
    protected ResponseEntity<?> handle(MissingPathVariableException e) {
        log.error("MissingPathVariableException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }

    @ExceptionHandler(PotholeNotFoundException.class)
    protected ResponseEntity<?> handle(PotholeNotFoundException e) {
        log.error("PotholeNotFoundException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }

    @ExceptionHandler(PotholeAreaNotFoundException.class)
    protected ResponseEntity<?> handle(PotholeAreaNotFoundException e) {
        log.error("PotholeNotFoundException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }

    @ExceptionHandler(PotholeLocationNotFoundException.class)
    protected ResponseEntity<?> handle(PotholeLocationNotFoundException e) {
        log.error("PotholeNotFoundException = {}", e.getErrorCode().getMessage());
        return response.error(e.getErrorCode());
    }
}
