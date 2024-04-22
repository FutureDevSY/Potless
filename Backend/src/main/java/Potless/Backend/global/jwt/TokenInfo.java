package Potless.Backend.global.jwt;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenInfo {

    private String email;
    private String accessToken;
    private String refreshToken;

}
