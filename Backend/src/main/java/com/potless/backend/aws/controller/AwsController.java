package com.potless.backend.aws.controller;


import com.potless.backend.aws.dto.request.S3FilePullRequest;
import com.potless.backend.aws.service.AwsService;
import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.path.dto.KakaoWaypointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    private final AwsService awsService;
    private final ApiResponse response;

    @Operation(summary = "이미지 다운로드", description = "이미지를 다운로드 합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이미지 다운로드 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/s3/download")
    public String downloadFile(@RequestBody S3FilePullRequest request) throws IOException {
        log.info("Downloading file with key: {}", request.getKey());
        awsService.downloadFile(request.getKey());
        return "File downloaded successfully";
    }

}
