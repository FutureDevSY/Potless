package com.potless.backend.damage.service;

import com.potless.backend.aws.service.AwsService;
import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.dto.service.request.ReDetectionRequestDTO;
import com.potless.backend.damage.dto.service.response.kakao.Address;
import com.potless.backend.damage.dto.service.response.kakao.RoadAddress;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
public class AsyncService {

    private final IDamageService iDamageService;
    private final KakaoService kakaoService;
    private final AwsService awsService;
    private final ReDetectionApiService detectionApiService;

    @Async
    public CompletableFuture<Void> setDamageAsyncMethod(DamageSetRequestDTO damageSetRequestDTO, List<MultipartFile> files, MultipartFile label) throws IOException {
        return CompletableFuture.runAsync(() -> {
            //중복 처리

            //fastApi 2차 탐지 요청 수행 및 결과 반환
            ReDetectionRequestDTO detectionRequestDTO = new ReDetectionRequestDTO(files.get(0), label);
            try {
                int severityResult = detectionApiService.reDetectionResponse(detectionRequestDTO);
                log.info("severity = {}", severityResult);
                damageSetRequestDTO.setSeverity(severityResult);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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

            damageSetRequestDTO.setImages(fileUrls);

            // 비동기로 처리하고 바로 응답 반환 검증
            kakaoService.fetchKakaoData(damageSetRequestDTO.getX(), damageSetRequestDTO.getY())
                    .thenAcceptAsync(data -> {
                        try {
                            RoadAddress roadAddress = data.getDocuments().get(0).getRoad_address();
                            Address address = data.getDocuments().get(0).getAddress();
                            String city = (address != null) ? address.getRegion_1depth_name() : roadAddress.getRegion_1depth_name();

                            String addressName = (address != null) ? address.getAddress_name() : roadAddress.getAddress_name();
                            String location;
                            String area;

                            if (city.equals("대전")) {
                                location = (address != null) ? address.getRegion_3depth_name() : "정보가 존재하지 않습니다";
                                area = (address != null) ? address.getRegion_2depth_name() : roadAddress.getRegion_2depth_name();
                            } else {
                                location = "기타";
                                area = "기타";
                            }

                            DamageSetRequestServiceDTO serviceDTO = DamageSetRequestServiceDTO.builder()
                                    .dirX(damageSetRequestDTO.getX())
                                    .dirY(damageSetRequestDTO.getY())
                                    .dtype(damageSetRequestDTO.getDtype())
                                    .width(10.000)
                                    .address(addressName)
                                    .severity(damageSetRequestDTO.getSeverity())
                                    .status(Status.작업전)
                                    .area(area)
                                    .location(location)
                                    .images(damageSetRequestDTO.getImages())
                                    .build();

                            iDamageService.setDamage(serviceDTO);
                        } catch (Exception e) {
                            for (String s : fileUrls)
                                awsService.deleteFile(s);
                            throw new PotholeNotFoundException();
                        }
                    });
        });
    }
}
