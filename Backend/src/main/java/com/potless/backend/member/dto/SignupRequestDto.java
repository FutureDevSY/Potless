package com.potless.backend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @Email(message = "올바른 형식의 이메일 주소를 입력해 주십시오.")
    @NotEmpty(message = "이메일 필드는 필수 정보입니다. 공란으로 두실 수 없습니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{4,20}$",
            message = "비밀번호는 영문 소문자, 숫자를 조합하여 4~20자 이내여야 합니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인란을 반드시 입력해 주셔야 합니다.")
    private String passwordConfirm;

    @Pattern(regexp = "^([가-힇]){2,5}$",
            message = "이름은 한글(자음 또는 모음만 존재하는 것 제외)을 조합하여 2~5자 이내여야 합니다.")
    private String memberName;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 유효하지 않습니다.")
    private String phone;

}
