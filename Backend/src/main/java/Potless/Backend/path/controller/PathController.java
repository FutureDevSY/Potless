package Potless.Backend.path.controller;

import Potless.Backend.global.format.code.ApiResponse;
import Potless.Backend.global.format.response.ResponseCode;
import Potless.Backend.path.dto.GetOptimalPathRequest;
import Potless.Backend.path.dto.KakaoWaypointResponse;
import Potless.Backend.path.service.PathService;
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