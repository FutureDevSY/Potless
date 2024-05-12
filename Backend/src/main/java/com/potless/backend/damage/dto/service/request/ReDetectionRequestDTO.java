package com.potless.backend.damage.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@AllArgsConstructor
@Getter
public class ReDetectionRequestDTO {

    private File image;

}
