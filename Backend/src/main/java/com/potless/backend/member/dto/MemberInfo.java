package com.potless.backend.member.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo {

    private Long Id;
    private String memberName;
    private Integer role;
    private String email;
    private String phone;
    private Integer region;
}
