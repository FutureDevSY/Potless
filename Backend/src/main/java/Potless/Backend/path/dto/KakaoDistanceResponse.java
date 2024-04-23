package Potless.Backend.path.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoDistanceResponse {

    private String transId;
    private List<Route> routes;

    @Getter
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Route {
        private Integer resultCode;
        private String resultMsg;
        private Summary summary;
    }

    @Getter
    public static class Summary {
        private Integer distance;
    }

}
