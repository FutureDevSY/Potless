package com.potless.backend.member.service;

import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.member.dto.LoginRequestDto;
import com.potless.backend.member.dto.LoginResponseDto;
import com.potless.backend.member.dto.SignupRequestDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface MemberService {

    public Long signup(SignupRequestDto requestDto);

    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse response, int identify);

    public String logout(String email, HttpServletResponse servletResponse, int identify);

}
