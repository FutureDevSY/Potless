package com.potless.backend.member.service;

import com.potless.backend.member.dto.LoginRequestDto;
import com.potless.backend.member.dto.LoginResponseDto;
import com.potless.backend.member.dto.SignupRequestDto;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    public Long signup(SignupRequestDto requestDto);

    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse response, int identify);

    public String logout(String email, HttpServletResponse servletResponse, int identify);

}
