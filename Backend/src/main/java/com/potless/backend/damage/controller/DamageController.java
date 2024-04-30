package com.potless.backend.damage.controller;


import com.potless.backend.aws.service.AwsService;
import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageVerificationRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.dto.service.response.StatisticCountResponseDTO;
import com.potless.backend.damage.dto.service.response.kakao.Address;
import com.potless.backend.damage.dto.service.response.kakao.RoadAddress;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


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
    private final AwsService awsService;

    @Operation(summary = "Damage 리스트 조회", description = "리스트 조회")
    @GetMapping
    public ResponseEntity<?> getDamages(
            Authentication authentication,
            @ModelAttribute DamageSearchRequestDTO damageSearchRequestDTO,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<DamageResponseDTO> damages = iDamageService.getDamages(damageSearchRequestDTO, pageable);
        return response.success(ResponseCode.POTHOLE_LIST_FETCHED, damages);
    }

    @Operation(summary = "Damage 조회", description = "조회")
    @GetMapping({"{damageId}"})
    public ResponseEntity<?> getDamage(Authentication authentication, @PathVariable Long damageId) {
        DamageResponseDTO damageResponseDTO = iDamageService.getDamage(damageId);
        return response.success(ResponseCode.POTHOLE_FETCHED, damageResponseDTO);
    }

    @Operation(summary = "Damage 삭제", description = "삭제")
    @DeleteMapping("{damageId}")
    public ResponseEntity<?> deleteDamage(Authentication authentication, @PathVariable Long damageId) {
        iDamageService.deleteDamage(damageId);
        return response.success(ResponseCode.POTHOLE_DELETED);
    }

    @Operation(summary = "Damage 구 별 통계 조회", description = "동 별 통계 조회")
    @GetMapping("statistic/{locationName}")
    public ResponseEntity<?> getStatistic(Authentication authentication, @PathVariable String locationName) {
        StatisticCountResponseDTO statistic = iDamageService.getStatistic(locationName);
        return response.success(ResponseCode.POTHOLE_STATISTIC_COUNT, statistic);
    }

    @Operation(summary = "Damage 동 별 통계 조회", description = "동 별 통계 조회")
    @GetMapping("statistic/location/{locationName}")
    public ResponseEntity<?> getStatisticLocation(Authentication authentication, @PathVariable String locationName) {
        StatisticCountResponseDTO statistic = iDamageService.getStatisticLocation(locationName);
        return response.success(ResponseCode.POTHOLE_STATISTIC_COUNT, statistic);
    }

    @Operation(summary = "Damage 동 전체 통계 조회", description = "동 별 조회")
    @GetMapping("statistic/location")
    public ResponseEntity<?> getStatisticLocations(Authentication authentication) {
        List<StatisticCountResponseDTO> statistic = iDamageService.getStatisticLocations();
        return response.success(ResponseCode.POTHOLE_STATISTIC_COUNT, statistic);
    }

    @Operation(summary = "Damage 삽입", description = "삽입")
    @PostMapping(value = "set", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> setDamage(
            Authentication authentication,
            @RequestPart("dtype") String dtype,
            @RequestPart("x") String x,
            @RequestPart("y") String y,
            @RequestPart("files") List<MultipartFile> files
    ) {
        DamageSetRequestDTO damageSetRequestDTO = DamageSetRequestDTO.builder()
                .dtype(dtype)
                .x(Double.valueOf(x))
                .y(Double.valueOf(y))
                .build();


        // 위험도 파악 비동기
        Map<String, String> fileUrlsAndKeys = files.stream()
                .map(file -> {
                    try {
                        return awsService.uploadFileToS3(file);
                    } catch (IOException e) {
                        log.error("Error uploading file to S3", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<String> fileUrls = new ArrayList<>(fileUrlsAndKeys.values()); // URL 리스트 추출

        damageSetRequestDTO.setImages(fileUrls);

        // 비동기로 처리하고 바로 응답 반환 검증
        kakaoService.fetchKakaoData(damageSetRequestDTO.getX(), damageSetRequestDTO.getY())
                .thenAcceptAsync(data -> {
                    try {
                        log.info("data = {}", data);
                        RoadAddress roadAddress = data.getDocuments().get(0).getRoad_address();
                        Address address = data.getDocuments().get(0).getAddress();

                        String addressName = (address != null) ? address.getAddress_name() : roadAddress.getAddress_name();
                        String location = (address != null) ? address.getRegion_3depth_name() : "정보가 존재하지 않습니다";
                        String area = (address != null) ? address.getRegion_2depth_name() : roadAddress.getRegion_2depth_name();

                        DamageVerificationRequestDTO verificationRequestDTO = DamageVerificationRequestDTO.builder()
                                .dtype(damageSetRequestDTO.getDtype())
                                .damageAddress(addressName)
                                .location(location)
                                .area(area)
                                .images(fileUrls)
                                .build();

                        List<DamageResponseDTO> damageVerification = iDamageService.getDamageVerification(verificationRequestDTO);
                        log.info("damageVerification = {}", damageVerification);
                        log.info("boolean = {}", iVerificationService.verificationDamage(damageVerification));
                        if (iVerificationService.verificationDamage(damageVerification)) {
                            // 이미지 위치 옮기기
                            List<String> newUrls = fileUrlsAndKeys.keySet().stream()
                                    .map(s -> awsService.moveFileToVerified(s, "검증후/" + s.substring(s.lastIndexOf('/') + 1)))
                                    .toList();
                            // width 구하기, 위험도 구하기 로직 추가 해야됨
                            DamageSetRequestServiceDTO serviceDTO = DamageSetRequestServiceDTO.builder()
                                    .dirX(damageSetRequestDTO.getX())
                                    .dirY(damageSetRequestDTO.getY())
                                    .dtype(damageSetRequestDTO.getDtype())
                                    .width(10.000)
                                    .address(addressName)
                                    .severity(1)
                                    .status(Status.작업전)
                                    .area(area)
                                    .location(location)
                                    .images(newUrls)
                                    .build();
                            log.info("serviceDTO = {}", serviceDTO);
                            iDamageService.setDamage(serviceDTO);
                        }
                    } catch (Exception e) {
                        log.error("An error occurred in thenAcceptAsync method", e);
                    }
                });

        return response.success(ResponseCode.POTHOLE_DETECTED);
    }
}
