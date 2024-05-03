package com.potless.backend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "Potless API 명세서",
                 description = "Potless 서비스 API 명세서"))
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .addServersItem(new Server().url("/"))
            .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
            .components(new Components()
                            .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT")));
    }

    // swagger 그룹 추가
    // 전체보기
    @Bean
    public GroupedOpenApi all() {
        return GroupedOpenApi.builder()
                             .group("a. 전체")
                             .pathsToMatch("/api/**")
                             .build();
    }

    @Bean
    public GroupedOpenApi memberGroup() {
        return GroupedOpenApi.builder()
                             .group("a. 회원")
                             .pathsToMatch("/api/member/**")
                             .build();
    }

    @Bean
    public GroupedOpenApi cocktailGroup() {
        return GroupedOpenApi.builder()
                .group("b. 도로파손")
                .pathsToMatch("/api/damage/**")
                .build();
    }

    @Bean
    public GroupedOpenApi diaryGroup() {
        return GroupedOpenApi.builder()
                .group("c. 경로")
                .pathsToMatch("/api/path/**")
                .build();
    }
    @Bean
    public GroupedOpenApi awsGroup() {
        return GroupedOpenApi.builder()
                .group("d. aws")
                .pathsToMatch("/api/aws/**")
                .build();
    }
}
