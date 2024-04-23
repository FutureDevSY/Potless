package Potless.Backend.path.controller;

import Potless.Backend.path.dto.GetOptimalPathRequest;
import Potless.Backend.path.dto.KakaoWaypointResponse;
import Potless.Backend.path.service.PathService;
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
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    @PostMapping("/optimal")
    public ResponseEntity<?> getOptimalPath(@RequestBody GetOptimalPathRequest getOptimalPathRequest) {
        return ResponseEntity.ok(pathService.getOptimalPath(getOptimalPathRequest));
    }

}