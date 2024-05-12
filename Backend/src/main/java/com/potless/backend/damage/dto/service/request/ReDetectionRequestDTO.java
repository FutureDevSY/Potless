package com.potless.backend.damage.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@AllArgsConstructor
@Getter
public class ReDetectionRequestDTO {

    private File image;

}
