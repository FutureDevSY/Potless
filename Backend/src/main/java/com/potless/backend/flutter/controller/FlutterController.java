package com.potless.backend.flutter.controller;

import com.potless.backend.aws.service.AwsService;
import com.potless.backend.damage.service.AsyncService;
import com.potless.backend.damage.service.FileService;
import com.potless.backend.damage.service.IAreaLocationService;
import com.potless.backend.damage.service.IDamageService;
import com.potless.backend.flutter.dto.controller.request.FlutterSendRequestDTO;
import com.potless.backend.flutter.dto.controller.response.CombinedResponseDTO;
import com.potless.backend.flutter.dto.service.response.DamageAppResponseDTO;
import com.potless.backend.flutter.dto.service.response.Point;
import com.potless.backend.flutter.service.KaKaoNaviService;
import com.potless.backend.global.format.code.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("api/flutter")
@RequiredArgsConstructor
public class FlutterController {

    private final IDamageService iDamageService;
    private final KaKaoNaviService kaKaoNaviService;
    private final ApiResponse response;
    private final AwsService awsService;
    private final IAreaLocationService iAreaLocationService;
    private final AsyncService asyncService;
    private final FileService fileService;

    @PostMapping("/find_route")
    public ResponseEntity<?> findRoute(
            Authentication authentication,
            @RequestBody FlutterSendRequestDTO request) {
        try {
            String kakaoResponse = kaKaoNaviService.fetchKakaoData(
                    request.getStartX(), request.getStartY(),
                    request.getEndX(), request.getEndY()
            ).get();

            if (kakaoResponse != null) {
                List<Point> coordinates = kaKaoNaviService.extractCoordinates(kakaoResponse);
                List<DamageAppResponseDTO> results = kaKaoNaviService.checkCoordinates(coordinates).get();
                log.info("kakaoResponse = {}", kakaoResponse);
                log.info("result = {}", results);
                return response.success(
                        CombinedResponseDTO.builder()
                                .kakaoResponse(kakaoResponse)
                                .results(results)
                                .build()
                );
            } else {
                return ResponseEntity.status(500).body("Error fetching Kakao navigation data");
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error processing request", e);
            return ResponseEntity.status(500).body("Error processing request");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
