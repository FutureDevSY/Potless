package com.potless.backend.member.service;

import com.potless.backend.member.dto.EmailValidationRequestDto;
import com.potless.backend.member.dto.LoginRequestDto;
import com.potless.backend.member.dto.LoginResponseDto;
import com.potless.backend.member.dto.SignupRequestDto;
import com.potless.backend.member.entity.MemberEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface MemberService {

    public Long signup(SignupRequestDto requestDto);

    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse response, int identify);

    public String logout(String email, HttpServletResponse servletResponse, int identify);

    public MemberEntity findMember(String email);

    public String extendAppLogin(Authentication authentication, HttpServletResponse httpServletResponse, int identify);
}
