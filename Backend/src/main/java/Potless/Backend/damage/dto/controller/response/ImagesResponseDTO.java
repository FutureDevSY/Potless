package Potless.Backend.damage.dto.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImagesResponseDTO {

    private Long id;
    private String url;
    private Integer order;

    @Builder
    @QueryProjection

    public ImagesResponseDTO(Long id, String url, Integer order) {
        this.id = id;
        this.url = url;
        this.order = order;
    }
}
