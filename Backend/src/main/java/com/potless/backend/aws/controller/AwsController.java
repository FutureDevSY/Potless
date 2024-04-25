package com.potless.backend.aws.controller;


import com.potless.backend.aws.dto.request.S3FilePullRequest;
import com.potless.backend.aws.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AwsController {

    private final S3StorageService s3StorageService;

    @PostMapping("/s3/download")
    public String downloadFile(@RequestBody S3FilePullRequest request) throws IOException {
        log.info("Downloading file with key: {}", request.getKey());
        s3StorageService.downloadFile(request.getKey());
        return "File downloaded successfully";
    }
}
