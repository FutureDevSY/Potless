package com.potless.backend.project.dto.request;

import com.potless.backend.path.dto.Location;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TaskAddRequestDto {
    @NotNull(message = "projectId는 필수값입니다.")
    private Long projectId;
    @NotNull(message = "damageIds는 필수값입니다.")
    private List<Long> damageIds;
    private Location origin;
}
