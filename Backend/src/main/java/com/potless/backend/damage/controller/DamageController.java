package com.potless.backend.damage.controller;


import com.potless.backend.aws.service.AwsService;
import com.potless.backend.damage.dto.controller.request.*;
import com.potless.backend.damage.dto.controller.response.AreaResponseDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.controller.response.LocationResponseDTO;
import com.potless.backend.damage.dto.service.request.AreaDamageCountForMonthServiceRequestDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.dto.service.response.*;
import com.potless.backend.damage.dto.service.response.kakao.Address;
import com.potless.backend.damage.dto.service.response.kakao.RoadAddress;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.service.IAreaLocationService;
import com.potless.backend.damage.service.IDamageService;
import com.potless.backend.damage.service.IVerificationService;
import com.potless.backend.damage.service.KakaoService;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private final IAreaLocationService iAreaLocationService;

    @Operation(summary = "Area 리스트 가져오기", description = "Area 리스트 가져오기", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AreaResponseDTO.class)))
    })
    @GetMapping("area")
    public ResponseEntity<?> getAreaList(Authentication authentication) {
        List<AreaResponseDTO> list = iAreaLocationService.getAreaList();
        return response.success(ResponseCode.AREA_LIST_FETCHED, list);
    }

    @Operation(summary = "Area 가져오기", description = "Area 가져오기", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AreaResponseDTO.class)))
    })
    @GetMapping("area/{areaId}")
    public ResponseEntity<?> getArea(
            Authentication authentication,
            @PathVariable Long areaId
    ) {
        AreaResponseDTO areaResponseDTO = iAreaLocationService.getAreaById(areaId);
        return response.success(ResponseCode.AREA_FETCHED, areaResponseDTO);
    }

    @Operation(summary = "Location 리스트 가져오기", description = "Location 리스트 가져오기", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = LocationResponseDTO.class)))
    })
    @GetMapping("location")
    public ResponseEntity<?> getLocationList(Authentication authentication) {
        List<LocationResponseDTO> list = iAreaLocationService.getLocationList();
        return response.success(ResponseCode.LOCATION_LIST_FETCHED, list);
    }

    @Operation(summary = "Location 가져오기", description = "Location 가져오기", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = LocationResponseDTO.class)))
    })
    @GetMapping("location/{locationId}")
    public ResponseEntity<?> getLocation(
            Authentication authentication,
            @PathVariable Long locationId
    ) {
        LocationResponseDTO locationResponseDTO = iAreaLocationService.getLocationById(locationId);
        return response.success(ResponseCode.LOCATION_FETCHED, locationResponseDTO);
    }

    @Operation(summary = "구별 월별 지정 발생한 도로 파손", description = "구별 월별 지정 발생한 도로 파손  (START 만 입력시 단일 조회 START, END 입력시 START ~ END 조회)", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "포트홀 구별 통계 조회 성공", content = @Content(schema = @Schema(implementation = AreaForMonthListResponseDTO.class)))
    })
    @GetMapping("damage/month/area")
    public ResponseEntity<?> getAreaDamageCountForMonth(
            Authentication authentication,
            @ModelAttribute AreaDamageCountForMonthRequestDTO areaDamageCountForMonthRequestDTO
    ) {

        // DTO 변환 로직
        YearMonth startMonth = YearMonth.parse(areaDamageCountForMonthRequestDTO.getStart(), DateTimeFormatter.ofPattern("yyyy-MM"));
        YearMonth endMonth = areaDamageCountForMonthRequestDTO.getEnd() != null ? YearMonth.parse(areaDamageCountForMonthRequestDTO.getEnd(), DateTimeFormatter.ofPattern("yyyy-MM")) : startMonth;

        AreaDamageCountForMonthServiceRequestDTO serviceRequestDTO = AreaDamageCountForMonthServiceRequestDTO.builder()
                .start(startMonth)
                .end(endMonth)
                .build();

        // 서비스 계층 호출
        AreaForMonthListResponseDTO areaDamageCountForMonth = iDamageService.getAreaDamageCountForMonth(serviceRequestDTO);

        // 결과 반환
        return response.success(ResponseCode.POTHOLE_AREA_DATE_COUNT, areaDamageCountForMonth);
    }

    @Operation(summary = "구별 날짜 지정 발생한 도로 파손", description = "구별 날짜 지정 발생한 도로 파손  ( START 만 입력시 단일 조회 START, END 입력시 START ~ END 조회 )", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "포트홀 구별 통계 조회 성공", content = @Content(schema = @Schema(implementation = DamageResponseDTO.class)))
    })
    @GetMapping("damage/date/area")
    public ResponseEntity<?> getAreaDamageCountForDate(
            Authentication authentication,
            @ModelAttribute AreaDamageCountForDateRequestDTO areaDamageCountForDateRequestDTO
    ) {
        AreaForDateListResponseDTO areaDamageCountForDate = iDamageService.getAreaDamageCountForDate(areaDamageCountForDateRequestDTO);
        return response.success(ResponseCode.POTHOLE_AREA_DATE_COUNT, areaDamageCountForDate);
    }

    @Operation(summary = "Damage 리스트 조회", description = "Damage 리스트를 조회합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "포트홀 조회 성공", content = @Content(schema = @Schema(implementation = DamageResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<?> getDamages(
            Authentication authentication,
            @ModelAttribute DamageSearchRequestDTO damageSearchRequestDTO,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<DamageResponseDTO> damages = iDamageService.getDamages(damageSearchRequestDTO, pageable);
        return response.success(ResponseCode.POTHOLE_LIST_FETCHED, damages);
    }

    @Operation(summary = "Damage 조회", description = "단일 Damage를 조회합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "단일 Damage 조회 성공", content = @Content(schema = @Schema(implementation = DamageResponseDTO.class)))
    })
    @GetMapping({"{damageId}"})
    public ResponseEntity<?> getDamage(Authentication authentication, @PathVariable Long damageId) {
        DamageResponseDTO damageResponseDTO = iDamageService.getDamage(damageId);
        return response.success(ResponseCode.POTHOLE_FETCHED, damageResponseDTO);
    }

    @Operation(summary = "Damage 삭제", description = "단일 Damage를 삭제합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "단일 Damage 삭제 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("{damageId}")
    public ResponseEntity<?> deleteDamage(Authentication authentication, @PathVariable Long damageId) {
        iDamageService.deleteDamage(damageId);
        return response.success(ResponseCode.POTHOLE_DELETED);
    }

    @Operation(summary = "구별 Damage 통계 조회", description = "구별 Damage 통계를 조회합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "구별 Damage 통계 조회 성공", content = @Content(schema = @Schema(implementation = StatisticCountResponseDTO.class)))
    })
    @GetMapping("statistic")
    public ResponseEntity<?> getStatistics(Authentication authentication) {
        List<StatisticCountResponseDTO> statistics = iDamageService.getStatistics();
        return response.success(ResponseCode.POTHOLE_STATISTICS_COUNT, statistics);
    }

    @Operation(summary = "단일 구의 동별 Damage 통계 조회", description = "단일 구의 동별 Damage 통계를 조회합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "단일 구의 동별 Damage 통계 조회 성공", content = @Content(schema = @Schema(implementation = StatisticListResponseDTO.class)))
    })
    @GetMapping("statistic/{areaId}")
    public ResponseEntity<?> getStatistic(Authentication authentication, @PathVariable Long areaId) {
        StatisticListResponseDTO statistic = iDamageService.getStatistic(areaId);
        return response.success(ResponseCode.POTHOLE_STATISTIC_COUNT, statistic);
    }

    @Operation(summary = "단일 동의 Damage 통계 조회", description = "단일 동의 Damage 통계를 조회합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "단일 동의 Damage 통계 조회 성공", content = @Content(schema = @Schema(implementation = StatisticLocationCountResponseDTO.class)))
    })
    @GetMapping("statistic/location/{locationName}")
    public ResponseEntity<?> getStatisticLocation(Authentication authentication, @PathVariable String locationName) {
        StatisticLocationCountResponseDTO statistic = iDamageService.getStatisticLocation(locationName);
        return response.success(ResponseCode.POTHOLE_STATISTIC_COUNT, statistic);
    }

    @Operation(summary = "전체 동의 Damage 통계 조회", description = "전체 동의 Damage 통계를 조회합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "전체 동의 Damage 통계 조회 성공", content = @Content(schema = @Schema(implementation = StatisticLocationCountResponseDTO.class)))
    })
    @GetMapping("statistic/location")
    public ResponseEntity<?> getStatisticLocations(Authentication authentication) {
        List<StatisticLocationCountResponseDTO> statistic = iDamageService.getStatisticLocations();
        return response.success(ResponseCode.POTHOLE_STATISTICS_COUNT, statistic);
    }

    @Operation(summary = "Damage 작업 중 사진 추가", description = "Damage의 작업 중 사진을 추가합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Damage의 작업 중 사진 추가 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping(value = "set/during", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> setDuringDamage(
            Authentication authentication,
            @RequestPart("damageId") String damageId,
            @RequestPart("files") List<MultipartFile> files
    ) {
        Map<String, String> fileUrlsAndKeys = files.stream()
                .map(file -> {
                    try {
                        String fileName = "AfterVerification/DuringWork/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        return awsService.uploadFileToS3(file, fileName);
                    } catch (IOException e) {
                        log.error("Error uploading file to S3", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<String> fileUrls = new ArrayList<>(fileUrlsAndKeys.values()); // URL 리스트 추출

        try {
            iDamageService.setImageForStatus(Long.valueOf(damageId), fileUrls);
        } catch (Exception e) {
            for (String s : fileUrls)
                awsService.deleteFile(s);
            throw new PotholeNotFoundException();
        }
        return response.success(ResponseCode.POTHOLE_DURING_WORK);
    }

    @Operation(summary = "Damage 작업 완료 사진 추가", description = "Damage의 작업 완료 사진을 추가합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Damage의 작업 완료 사진 추가 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping(value = "set/after", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> setAfterDamage(
            Authentication authentication,
            @RequestPart("damageId") String damageId,
            @RequestPart("files") List<MultipartFile> files) {

        Map<String, String> fileUrlsAndKeys = files.stream()
                .map(file -> {
                    try {
                        String fileName = "AfterVerification/AfterWork/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        return awsService.uploadFileToS3(file, fileName);
                    } catch (IOException e) {
                        log.error("Error uploading file to S3", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<String> fileUrls = new ArrayList<>(fileUrlsAndKeys.values()); // URL 리스트 추출

        try {
            iDamageService.setImageForStatus(Long.valueOf(damageId), fileUrls);
        } catch (Exception e) {
            for (String s : fileUrls)
                awsService.deleteFile(s);
            throw new PotholeNotFoundException();
        }

        return response.success(ResponseCode.POTHOLE_AFTER_WORK);
    }

    @Operation(summary = "Damage 작업 완료 상태 전환", description = "Damage의 상태를 작업 완료로 전환합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Damage의 상태 전환 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("workDone")
    public ResponseEntity<?> setWorkDone(Authentication authentication, @RequestBody Long damageId) {
        iDamageService.setWorkDone(damageId);
        return response.success(ResponseCode.POTHOLE_DONE_WORK);
    }

    @Operation(summary = "Damage 수동 삽입", description = "Damage를 삽입합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Damage 삽입 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping(value = "setManual")
    public ResponseEntity<?> setManualDamage(
            Authentication authentication,
            @Validated @RequestBody DamageManualRequestDTO request,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        // 비동기로 처리하고 바로 응답 반환 검증
        kakaoService.fetchKakaoData(request.getX(), request.getY())
                .thenAcceptAsync(data -> {
                    RoadAddress roadAddress = data.getDocuments().get(0).getRoad_address();
                    Address address = data.getDocuments().get(0).getAddress();

                    String addressName = (address != null) ? address.getAddress_name() : roadAddress.getAddress_name();
                    String location = (address != null) ? address.getRegion_3depth_name() : "정보가 존재하지 않습니다";
                    String area = (address != null) ? address.getRegion_2depth_name() : roadAddress.getRegion_2depth_name();

                    DamageSetRequestDTO damageSetRequestDTO = DamageSetRequestDTO.builder()
                            .dtype(request.getType())
                            .x(request.getX())
                            .y(request.getY())
                            .build();

                    damageSetRequestDTO.setImages(Collections.singletonList("https://mine702-amazon-s3.s3.ap-northeast-2.amazonaws.com/Default/default.jpg"));

                    DamageSetRequestServiceDTO serviceDTO = DamageSetRequestServiceDTO.builder()
                            .dirX(damageSetRequestDTO.getX())
                            .dirY(damageSetRequestDTO.getY())
                            .dtype(damageSetRequestDTO.getDtype())
                            .width(0.0)
                            .address(addressName)
                            .severity(request.getSeverity())
                            .status(Status.작업전)
                            .area(area)
                            .location(location)
                            .images(damageSetRequestDTO.getImages())
                            .build();

                    iDamageService.setDamage(serviceDTO);
                });

        return response.success(ResponseCode.POTHOLE_DETECTED);
    }

    @Operation(summary = "Damage 삽입", description = "Damage를 삽입합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Damage 삽입 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
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
                        String fileName = "BeforeVerification/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        return awsService.uploadFileToS3(file, fileName);
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
                        if (iVerificationService.verificationDamage(damageVerification)) {
                            // 이미지 위치 옮기기
                            List<String> newUrls = fileUrlsAndKeys.keySet().stream()
                                    .map(s -> awsService.moveFileToVerified(s, "AfterVerification/BeforeWork/" + s.substring(s.lastIndexOf('/') + 1)))
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
                            iDamageService.setDamage(serviceDTO);
                        }
                    } catch (Exception e) {
                        for (String s : fileUrls)
                            awsService.deleteFile(s);
                        throw new PotholeNotFoundException();
                    }
                });

        return response.success(ResponseCode.POTHOLE_DETECTED);
    }
}
