package com.potless.backend.aws.service;

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
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3StorageService {

    private final AmazonS3 s3Client;

    @Value("${aws.s3-bucket-name}")
    private String bucketName;

    public void downloadFile(String keyName) throws IOException {
        S3Object s3object = s3Client.getObject(bucketName, keyName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        FileOutputStream outputStream = new FileOutputStream(new File(keyName));
        byte[] read_buf = new byte[1024];
        int read_len = 0;
        while ((read_len = inputStream.read(read_buf)) > 0) {
            outputStream.write(read_buf, 0, read_len);
        }
        outputStream.close();
        inputStream.close();
    }

    public String uploadFileToS3(MultipartFile file) throws IOException {
        String fileName = "검증전" + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File localFile = convertMultiPartToFile(file);
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, localFile));
        if (!localFile.delete()) {
            log.error("Failed to delete temporary file: {}", localFile.getPath());
            // 여기서 추가적인 오류 처리를 수행할 수 있습니다. 예를 들어, 다시 시도하거나, 관리자에게 알림 등
        } else {
            log.info("Temporary file deleted successfully: {}", localFile.getPath());
        }
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    public void moveFileToVerified(String sourceKey, String destinationKey) {
        // Copy the file
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, sourceKey, bucketName, destinationKey);
        s3Client.copyObject(copyObjRequest);

        // Delete the original file
        DeleteObjectRequest deleteObjRequest = new DeleteObjectRequest(bucketName, sourceKey);
        s3Client.deleteObject(deleteObjRequest);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}