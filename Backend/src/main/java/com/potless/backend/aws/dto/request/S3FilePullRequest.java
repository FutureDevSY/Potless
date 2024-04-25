package com.potless.backend.aws.dto.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class S3FilePullRequest {
    private String key;

    @Builder
    public S3FilePullRequest(String key) {
        this.key = key;
    }
}
