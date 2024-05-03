package com.potless.backend.project.dto.request;

import com.potless.backend.path.dto.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskDeleteRequestDto {

    private Long taskId;
    private Location origin;

}
