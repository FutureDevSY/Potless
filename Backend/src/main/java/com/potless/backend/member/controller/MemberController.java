package com.potless.backend.member.controller;

import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.global.jwt.service.TokenService;
import com.potless.backend.member.dto.EmailCheckRequestDto;
import com.potless.backend.member.dto.EmailValidationRequestDto;
import com.potless.backend.member.dto.LoginRequestDto;
import com.potless.backend.member.dto.SignupRequestDto;
import com.potless.backend.member.service.MailService;
import com.potless.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "Member 컨트롤러", description = "Member Controller API")
public class MemberController {

    private final TokenService tokenService;
    private final MemberService memberService;
    private final ApiResponse response;
    private final MailService mailService;

    /* 일반 회원가입 */
    @Operation(summary = "회원가입", description = "일반 사용자 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto requestDto,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        Long savedId = memberService.signup(requestDto);
        return response.success(ResponseCode.MEMBER_SIGNUP_SUCCESS, savedId);
    }

    /* 이메일 인증코드 요청 */
    @Operation(summary = "이메일 인증", description = "회원가입 시 이메일 인증 요청")
    @PostMapping("/email/verification")
    public ResponseEntity<?> emailVerification(@Valid @RequestBody EmailValidationRequestDto requestDto,
                                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        mailService.sendEmailVerification(requestDto.getEmail());
        return response.success(ResponseCode.EMAIL_VERIFICATION_SENT.getMessage());
    }


    /* 이메일 인증코드 인증처리 */
    @PostMapping("/email/validation")
    @Operation(summary = "이메일 확인", description = "이메일 인증 요청 확인")
    public ResponseEntity<?> emailVerify(@Valid @RequestBody EmailCheckRequestDto requestDto,
                                         BindingResult bindingResult,
                                         HttpServletResponse servletResponse) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        return response.success(ResponseCode.EMAIL_VERIFIED_SUCCESS,
                mailService.confirmAuthCode(requestDto.getEmail(), requestDto.getAuthNum(), servletResponse));
    }

    /* 로그인 요청 web/app 구별, refresh token 만료기간 차이*/
    @PostMapping("/login-web")
    @Operation(summary = "웹 로그인", description = "웹 로그인 요청")
    public ResponseEntity<?> loginWeb(@Valid @RequestBody LoginRequestDto requestDto,
                                      BindingResult bindingResult,
                                      HttpServletResponse httpServletResponse) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        return response.success(ResponseCode.LOGIN_SUCCESS_WEB, memberService.login(requestDto, httpServletResponse, 0));
    }

    @PostMapping("/login-app")
    @Operation(summary = "앱 로그인", description = "앱 로그인 요청")
    public ResponseEntity<?> loginApp(@Valid @RequestBody LoginRequestDto requestDto,
                                      BindingResult bindingResult,
                                      HttpServletResponse httpServletResponse) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        return response.success(ResponseCode.LOGIN_SUCCESS_APP, memberService.login(requestDto, httpServletResponse, 1));
    }

    @PostMapping("/logout-web")
    @Operation(summary = "웹 로그아웃", description = "웹 로그아웃 요청")
    public ResponseEntity<?> logoutWEB(Authentication authentication, HttpServletResponse servletResponse) {
        return response.success(ResponseCode.LOGOUT_SUCCESS, memberService.logout(authentication.getName(), servletResponse, 0));
    }

    @PostMapping("/logout-app")
    @Operation(summary = "앱 로그아웃", description = "앱 로그아웃 요청")
    public ResponseEntity<?> logoutApp(Authentication authentication, HttpServletResponse servletResponse) {
        return response.success(ResponseCode.LOGOUT_SUCCESS, memberService.logout(authentication.getName(), servletResponse, 1));
    }

    @PutMapping
    @Operation(summary = "토큰 재발급", description = "토큰 재발급 요청")
    public ResponseEntity<?> token(@RequestBody String accessToken, int identify) {
        return response.success(tokenService.reIssueAccessToken(accessToken, identify));
    }

}
