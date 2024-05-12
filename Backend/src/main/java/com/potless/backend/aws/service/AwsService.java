package com.potless.backend.aws.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsService {

    private final AmazonS3 s3Client;

    @Value("${aws.s3-bucket-name}")
    private String bucketName;


    public void downloadFile(String keyName) throws IOException {
        S3Object s3object = s3Client.getObject(bucketName, keyName);
        try (S3ObjectInputStream inputStream = s3object.getObjectContent();
             FileOutputStream outputStream = new FileOutputStream(new File(keyName))) {
            byte[] readBuf = new byte[1024];
            int readLen;
            while ((readLen = inputStream.read(readBuf)) > 0) {
                outputStream.write(readBuf, 0, readLen);
            }
        }
    }

    public Map<String, String> uploadFileToS3(File localFile, String fileName) throws IOException {
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, localFile));
        try {
            Files.delete(localFile.toPath());
            log.info("Temporary file deleted successfully: {}", localFile.getPath());
        } catch (IOException e) {
            log.error("Failed to delete temporary file: {}", localFile.getPath(), e);
        }

        String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
        Map<String, String> fileData = new HashMap<>();
        fileData.put(fileName, fileUrl);
        return fileData;
    }

    public Map<String, String> uploadFileToS3(MultipartFile file, String fileName) throws IOException {
        File localFile = convertMultiPartToFile(file);
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, localFile));
        try {
            Files.delete(localFile.toPath());
            log.info("Temporary file deleted successfully: {}", localFile.getPath());
        } catch (IOException e) {
            log.error("Failed to delete temporary file: {}", localFile.getPath(), e);
        }

        String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
        Map<String, String> fileData = new HashMap<>();
        fileData.put(fileName, fileUrl);
        return fileData;
    }

    public Map<String, String> uploadFileToS3(File localFile, String fileName) throws IOException {
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, localFile));
        try {
            Files.delete(localFile.toPath());
            log.info("Temporary file deleted successfully: {}", localFile.getPath());
        } catch (IOException e) {
            log.error("Failed to delete temporary file: {}", localFile.getPath(), e);
        }

        String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
        Map<String, String> fileData = new HashMap<>();
        fileData.put(fileName, fileUrl);
        return fileData;
    }


    public String moveFileToVerified(String sourceKey, String destinationKey) {
        // 파일을 새 위치로 복사
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, sourceKey, bucketName, destinationKey);
        s3Client.copyObject(copyObjRequest);

        // 원본 파일 삭제
        DeleteObjectRequest deleteObjRequest = new DeleteObjectRequest(bucketName, sourceKey);
        s3Client.deleteObject(deleteObjRequest);

        // 새 위치의 파일 URL 반환
        return s3Client.getUrl(bucketName, destinationKey).toString();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }


    public void deleteFile(String sourceKey) {
        try {
            // URL에서 키 부분만 추출
            URI uri = new URI(sourceKey);
            String path = uri.getPath();
            String key = path.substring(path.indexOf('/') + 1); // 첫 번째 '/' 이후의 문자열이 키

            DeleteObjectRequest deleteObjRequest = new DeleteObjectRequest(bucketName, key);
            s3Client.deleteObject(deleteObjRequest);
        } catch (URISyntaxException e) {
            log.error("Error parsing URI", e);
        } catch (AmazonServiceException e) {
            log.error("Error deleting file: " + sourceKey, e);
        } catch (SdkClientException e) {
            log.error("Client error in connecting to S3", e);
        }
    }

}