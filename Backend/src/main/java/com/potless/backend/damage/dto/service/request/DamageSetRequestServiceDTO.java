package com.potless.backend.damage.dto.service.request;

import com.potless.backend.damage.entity.enums.Status;
import lombok.*;

import java.util.List;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageSetRequestServiceDTO {

    private Long id;
    private Integer severity;
    private Double dirX;
    private Double dirY;
    private String address;
    private String roadName;
    private Double width;
    private Status status;
    private String area;
    private String location;
    private List<String> images;
    private String dtype;


    @Builder
    public DamageSetRequestServiceDTO(Long id, Integer severity, Double dirX, Double dirY, String address, String roadName, Double width, Status status, String area, String location, List<String> images, String dtype) {
        this.id = id;
        this.severity = severity;
        this.dirX = dirX;
        this.dirY = dirY;
        this.address = address;
        this.roadName = roadName;
        this.width = width;
        this.status = status;
        this.area = area;
        this.location = location;
        this.images = images;
        this.dtype = dtype;
    }
}