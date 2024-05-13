package com.potless.backend.damage.service;

import com.potless.backend.aws.service.AwsService;
import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.dto.service.request.ReDetectionRequestDTO;
import com.potless.backend.damage.dto.service.response.kakao.Address;
import com.potless.backend.damage.dto.service.response.kakao.RoadAddress;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.DuplPotholeException;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.hexagon.service.H3Service;
import com.potless.backend.hexagon.service.HexagonService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@EnableAsync
@RequiredArgsConstructor
public class AsyncService {

    private final IDamageService iDamageService;
    private final KakaoService kakaoService;
    private final AwsService awsService;
    private final ReDetectionApiService detectionApiService;
    private final DamageRepository damageRepository;
    private final H3Service h3Service;
    private final HexagonService hexagonService;

    @Async
    public void setDamageAsyncMethod(DamageSetRequestDTO damageSetRequestDTO, File imageFile) throws IOException {
        try {
            int res = 13;
            String hexagonIndex = h3Service.getH3Index(damageSetRequestDTO.getY(), damageSetRequestDTO.getX(), res);
            if (damageRepository.findDamageByHexagonIndexAndDtype(hexagonIndex, damageSetRequestDTO.getDtype())) {
                throw new DuplPotholeException();
            }
            //fastApi 2차 탐지 요청 수행 및 결과 반환
            ReDetectionRequestDTO detectionRequestDTO = new ReDetectionRequestDTO(imageFile);

            int severityResult = detectionApiService.reDetectionResponse(detectionRequestDTO);
<<<<<<< HEAD
            log.info("severity = {}", severityResult);
=======
>>>>>>> dev-BE
            damageSetRequestDTO.setSeverity(severityResult);

            String fileName = "AfterVerification/BeforeWork/" + System.currentTimeMillis() + "_" + imageFile.getName();
            Map<String, String> fileUrlAndKey = awsService.uploadFileToS3(imageFile, fileName);

            List<String> fileUrls = new ArrayList<>(fileUrlAndKey.values()); // URL 리스트 추출

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

                            serviceDTO.setHexagonIndex(hexagonIndex);

                            iDamageService.setDamage(serviceDTO);
                        } catch (Exception e) {
                            for (String s : fileUrls)
                                awsService.deleteFile(s);
                            throw new PotholeNotFoundException();
                        }
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
