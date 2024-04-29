package com.potless.backend.member.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamAddRequestDto {
    private Long projectId;

    private Long teamId;
}
