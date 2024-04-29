package com.potless.backend.aws.controller;


import com.potless.backend.aws.dto.request.S3FilePullRequest;
import com.potless.backend.aws.service.S3StorageService;
import com.potless.backend.global.format.code.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.potless.backend.global.format.response.ErrorCode.FAILED_TO_UPLOAD;
import static com.potless.backend.global.format.response.ResponseCode.SUCCESS_TO_UPLOAD;

@Slf4j
@RestController
@RequestMapping("api/aws")
@RequiredArgsConstructor
@Tag(name = "AWS 컨트롤러", description = "AWS Controller API")
public class AwsController {

    private final S3StorageService s3StorageService;
    private final ApiResponse response;

    @PostMapping("/s3/download")
    public String downloadFile(@RequestBody S3FilePullRequest request) throws IOException {
        log.info("Downloading file with key: {}", request.getKey());
        s3StorageService.downloadFile(request.getKey());
        return "File downloaded successfully";
    }

    @Operation(summary = "AWS S3 이미지 업로드", description = "이미지 업로드")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3StorageService.uploadFileToS3(file);
            return response.success(SUCCESS_TO_UPLOAD, fileUrl);

            //검증
//            boolean isVerified = performVerification(file);
//            if (isVerified) {
//                // Move to the "post-verification" folder
//                String postVerificationKey = "검증후/" + preVerificationKey.substring(preVerificationKey.lastIndexOf('/') + 1);
//                s3StorageService.moveFileToVerified(preVerificationKey, postVerificationKey);
//            }
        } catch (Exception e) {
            log.error("Failed to upload file", e);
            return response.error(FAILED_TO_UPLOAD);
        }
    }
}
