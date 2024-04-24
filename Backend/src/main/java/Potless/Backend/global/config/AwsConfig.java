package Potless.Backend.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    private static final String awsId = System.getenv("AWS_ACCESS_KEY_ID");
    private static final String awsKey = System.getenv("AWS_SECRET_ACCESS_KEY");
    private static final String region = System.getenv("AWS_REGION");

    @Bean
    public AmazonS3 s3client() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsId, awsKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }
}
