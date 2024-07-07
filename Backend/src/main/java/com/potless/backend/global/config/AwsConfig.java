package com.potless.backend.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${aws.secret-access-key}")
    private String awsKey;
    @Value("${aws.region}")
    private String region;
    @Value("${aws.access-key-id}")
    private String awsId;
    @Value("$aws.endpoint")
    private String endpoint;

    @Bean
    public AmazonS3 s3client() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsId, awsKey);
        return AmazonS3ClientBuilder.standard()
//                .withRegion(region)
                .withEndpointConfiguration(
                        new EndpointConfiguration(endpoint, region)
                )
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }
}
