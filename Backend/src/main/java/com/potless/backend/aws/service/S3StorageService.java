package com.potless.backend.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
}