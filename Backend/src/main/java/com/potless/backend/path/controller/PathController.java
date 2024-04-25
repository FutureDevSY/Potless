package com.potless.backend.path.controller;

import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.path.dto.GetOptimalPathRequest;
import com.potless.backend.path.service.PathService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/path")
public class PathController {

    private final PathService pathService;
    private final ApiResponse response;

    @PostMapping("/optimal")
    public ResponseEntity<?> getOptimalPath(@Valid @RequestBody GetOptimalPathRequest getOptimalPathRequest,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }
        return response.success(ResponseCode.OPTIMAL_PATH_FOUND, pathService.getOptimalPath(getOptimalPathRequest));
    }

}