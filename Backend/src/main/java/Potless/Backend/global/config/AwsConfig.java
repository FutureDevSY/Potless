package Potless.Backend.global.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

    @Bean
    public AWSCognitoIdentityProvider cognitoIdentityProvider() {
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2) // 사용자 풀이 위치한 리전
                .build();
    }

    @Bean
    public S3Client s3Client() {
        Region region = Region.of("ap-northeast-2"); // 여기에 사용하고 있는 AWS 리전을 넣으세요.
        return S3Client.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
