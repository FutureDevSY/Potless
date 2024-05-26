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
import com.potless.backend.damage.service.*;
import com.potless.backend.damage.websocket.WebSocketService;
import com.potless.backend.global.exception.kakao.KakaoNotFoundException;
import com.potless.backend.global.exception.member.MemberNotFoundException;
import com.potless.backend.global.exception.pothole.InvalidCoordinateRangeException;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletionException;
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
    private final AwsService awsService;
    private final IAreaLocationService iAreaLocationService;
    private final AsyncService asyncService;
    private final FileService fileService;
    private final DuplicateAreaService duplicateAreaService;
    private final MemberService memberService;
    private final WebSocketService webSocketService;

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
    @DeleteMapping
    public ResponseEntity<?> deleteDamage(Authentication authentication, @RequestBody DamageDeleteRequestDTO requestDTO) {
        List<String> strings = iDamageService.deleteDamage(requestDTO.getDamageIds());
        strings.forEach(awsService::deleteFile);
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

    @Operation(summary = "구 ID 를 활용 하여 구의 심각도 통계 조회", description = "구 ID 를 활용 하여 심각도 통계 조회.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "구 ID 를 활용 하여 심각도 통계 조회", content = @Content(schema = @Schema(implementation = StatisticListResponseDTO.class)))
    })
    @GetMapping("severity/{areaId}")
    public ResponseEntity<?> getSeverity(Authentication authentication, @PathVariable Long areaId) {
        SeverityAreaResponseDTO severity = iDamageService.getSeverity(areaId);
        return response.success(ResponseCode.POTHOLE_STATISTIC_COUNT, severity);
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

    @Operation(summary = "Damage 사진 변경 추가", description = "Damage의 사진을 변경합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Damage의 사진 변경 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping(value = "change/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> setChangeImage(
            Authentication authentication,
            @RequestPart("damageId") String damageId,
            @RequestPart("files") List<MultipartFile> files
    ) {
        Map<String, String> fileUrlsAndKeys = files.stream()
                .map(file -> {
                    try {
                        String fileName = "AfterVerification/BeforeWork/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
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
            List<String> strings = iDamageService.setChangeImage(Long.valueOf(damageId), fileUrls);
            strings.forEach(awsService::deleteFile);
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
    @PostMapping("/workDone")
    public ResponseEntity<?> setWorkDone(Authentication authentication, @RequestBody @Valid DamageDoneRequestDTO requestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }
        iDamageService.setWorkDone(requestDTO.getDamageId());
        webSocketService.sendWebSocket(authentication, requestDTO.getDamageId());
        return response.success(ResponseCode.POTHOLE_DONE_WORK);
    }

    @Operation(summary = "Damage 수동 삽입", description = "Damage를 삽입합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Damage 삽입 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping(value = "setManual", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> setManualDamage(
            Authentication authentication,
            @RequestPart("dtype") @NotNull String dtype,
            @RequestPart("x") @NotNull String x,
            @RequestPart("y") @NotNull String y,
            @RequestPart("severity") @NotNull String severity,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        double xValue = Double.parseDouble(x);
        double yValue = Double.parseDouble(y);
        if ((xValue <= 100 || xValue >= 140) || (yValue <= 20 || yValue >= 50)) {
            throw new InvalidCoordinateRangeException();
        }
        KakaoMapApiResponseDTO data;
        try {
            data = kakaoService.fetchKakaoData(xValue, yValue).join();
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof KakaoNotFoundException) {
                throw (KakaoNotFoundException) cause;
            }
            throw new RuntimeException("Error processing Kakao data", e);
        }
        if (data == null || data.getMeta().getTotal_count() == 0) {
            throw new KakaoNotFoundException();
        }
        RoadAddress roadAddress = data.getDocuments().get(0).getRoad_address();
        Address address = data.getDocuments().get(0).getAddress();
        String addressName = (address != null) ? address.getAddress_name() : roadAddress.getAddress_name();
        String location = (address != null) ? address.getRegion_3depth_name() : "정보가 존재하지 않습니다";
        String area = (address != null) ? address.getRegion_2depth_name() : roadAddress.getRegion_2depth_name();
        DamageSetRequestDTO damageSetRequestDTO = DamageSetRequestDTO.builder()
                .dtype(dtype)
                .x(xValue)
                .y(yValue)
                .build();
        if (files == null || files.isEmpty()) {
            damageSetRequestDTO.setImages(Collections.singletonList("https://mine702-amazon-s3.s3.ap-northeast-2.amazonaws.com/Default/default.jpg"));
        } else {
            Map<String, String> fileUrlsAndKeys = files.stream()
                    .map(file -> {
                        try {
                            String fileName = "AfterVerification/BeforeWork/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                            return awsService.uploadFileToS3(file, fileName);
                        } catch (IOException e) {
                            log.error("Error uploading file to S3", e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .flatMap(map -> map.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            List<String> fileUrls = new ArrayList<>(fileUrlsAndKeys.values());
            damageSetRequestDTO.setImages(fileUrls);
        }
        if (memberService.findMember(authentication.getName()) == null) {
            throw new MemberNotFoundException();
        }
        DamageSetRequestServiceDTO serviceDTO = DamageSetRequestServiceDTO.builder()
                .dirX(damageSetRequestDTO.getX())
                .dirY(damageSetRequestDTO.getY())
                .dtype(damageSetRequestDTO.getDtype())
                .width(0.0)
                .address(addressName)
                .severity(Integer.valueOf(severity))
                .status(Status.작업전)
                .area(area)
                .location(location)
                .images(damageSetRequestDTO.getImages())
                .memberId(memberService.findMember(authentication.getName()).getId())
                .build();

        iDamageService.setDamage(serviceDTO);
        return response.success(ResponseCode.POTHOLE_DETECTED);
    }

    @Operation(summary = "Damage 삽입", description = "Damage를 삽입합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Damage 삽입 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping(value = "set", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> setDamage(
            Authentication authentication,
            @RequestPart("dtype") @NotNull String dtype,
            @RequestPart("x") @NotNull String x,
            @RequestPart("y") @NotNull String y,
            @RequestPart("files") @NotNull List<MultipartFile> files
    ) throws IOException {
        double xValue = Double.parseDouble(x);
        double yValue = Double.parseDouble(y);
        if ((xValue <= 100 || xValue >= 140) || (yValue <= 20 || yValue >= 50)) {
            throw new InvalidCoordinateRangeException();
        }

        DamageSetRequestDTO damageSetRequestDTO = DamageSetRequestDTO.builder()
                .dtype(dtype)
                .x(xValue)
                .y(yValue)
                .build();

        String hexagonIndex = duplicateAreaService.checkIsDuplicated(damageSetRequestDTO);

        File imageFile = fileService.convertAndSaveFile(files.get(0));

        damageSetRequestDTO.setMemberId(memberService.findMember(authentication.getName()).getId());

        if (damageSetRequestDTO.getMemberId() == null) {
            throw new MemberNotFoundException();
        }

        asyncService.setDamageAsyncMethod(damageSetRequestDTO, imageFile, hexagonIndex);

        return response.success(ResponseCode.POTHOLE_DETECTED);
    }

}
