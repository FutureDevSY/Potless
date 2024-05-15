package com.potless.backend.damage.service;

import com.potless.backend.aws.service.AwsService;
import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.dto.service.request.ReDetectionRequestDTO;
import com.potless.backend.damage.dto.service.response.ReDetectionResponseDTO;
import com.potless.backend.damage.dto.service.response.kakao.Address;
import com.potless.backend.damage.dto.service.response.kakao.RoadAddress;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.DuplPotholeException;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.hexagon.repository.HexagonRepository;
import com.potless.backend.hexagon.service.H3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Log4j2
@Service
@EnableAsync
@RequiredArgsConstructor
@Transactional
public class AsyncService {

    private final IDamageService iDamageService;
    private final KakaoService kakaoService;
    private final AwsService awsService;
    private final ReDetectionApiService detectionApiService;
    private final H3Service h3Service;
    private final HexagonRepository hexagonRepository;
    private final DamageRepository damageRepository;
    private final FileService fileService;

    @Async
    public void setDamageAsyncMethod(DamageSetRequestDTO damageSetRequestDTO, File imageFile) throws IOException {
        try {
            int res = 13;
            String hexagonIndex = h3Service.getH3Index(damageSetRequestDTO.getY(), damageSetRequestDTO.getX(), res);

            hexagonRepository.findByHexagonIndex(hexagonIndex);

            Optional<DamageEntity> optionalDamageEntity = damageRepository.findDamageByHexagonIndexAndDtype(hexagonIndex, damageSetRequestDTO.getDtype());

            if (optionalDamageEntity.isPresent()) {
                DamageEntity damageEntity = optionalDamageEntity.get();
                if (!Objects.equals(damageEntity.getDirX(), damageSetRequestDTO.getX()) && !Objects.equals(damageEntity.getDirY(), damageSetRequestDTO.getY())) {
                    damageEntity.addCount();
                    damageRepository.save(damageEntity);
                }
                log.error("Duplicate pothole detected: {}", damageSetRequestDTO);
                throw new DuplPotholeException();
            }

            //fastApi 2차 탐지 요청 수행 및 결과 반환
            ReDetectionRequestDTO detectionRequestDTO = new ReDetectionRequestDTO(imageFile);
            ReDetectionResponseDTO detectionResult = detectionApiService.reDetectionResponse(detectionRequestDTO);

            // 1차 탐지 후 BeforeVerification/ 에 사진 담기
            String fileName = "BeforeVerification/" + System.currentTimeMillis() + "_" + imageFile.getName();
            Map<String, String> fileUrlAndKey = awsService.uploadFileToS3(imageFile, fileName);
            List<String> fileUrls = new ArrayList<>(fileUrlAndKey.values());
            log.info("fileName = {}", fileName);
            log.info("fileUrlAndKey = {}", fileUrlAndKey);
            for (String fileUrl : fileUrls) {
                log.info("fileUrl = {}", fileUrl);
            }

            damageSetRequestDTO.setSeverity(detectionResult.getSeverity());
            damageSetRequestDTO.setWidth((double) detectionResult.getWidth());

            // 2차 탐지 성공하면 AfterVerification/BeforeWork/ 파일로 이동
            List<String> newFileUrls = new ArrayList<>();
            for (String fileUrl : fileUrls) {
                String destinationKey = "AfterVerification/BeforeWork/" + new File(fileUrl).getName();
                String newUrl = awsService.moveFileToVerified(fileName, destinationKey);
                newFileUrls.add(newUrl);
                log.info("fileUrl = {}", fileUrl);
                log.info("newUrl = {}", newUrl);
            }

            damageSetRequestDTO.setImages(newFileUrls);

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
                                    .width(damageSetRequestDTO.getWidth())
                                    .address(addressName)
                                    .severity(damageSetRequestDTO.getSeverity())
                                    .status(Status.작업전)
                                    .area(area)
                                    .location(location)
                                    .width(damageSetRequestDTO.getWidth())
                                    .images(damageSetRequestDTO.getImages())
                                    .build();

                            serviceDTO.setHexagonIndex(hexagonIndex);

                            iDamageService.setDamage(serviceDTO);
                        } catch (Exception e) {
                            for (String s : newFileUrls)
                                awsService.deleteFile(s);
                            log.error("Error processing Kakao data: {}", e.getMessage());
                            throw new PotholeNotFoundException();
                        }
                    });

        } catch (DuplPotholeException e) {
            // Duplicate pothole exception handling
            log.error("Duplicate pothole exception: {}", e.getMessage());
        } catch (WebClientResponseException e) {
            log.error("WebClient error: Status code {} - Body {}", e.getRawStatusCode(), e.getResponseBodyAsString());
        } catch (IOException e) {
            fileService.deleteFile(imageFile);
            log.error("IO error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
        }
    }
}
