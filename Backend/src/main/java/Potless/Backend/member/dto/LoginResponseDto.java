package Potless.Backend.member.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDto {

    private String token;
    private MemberInfo memberInfo;
}
