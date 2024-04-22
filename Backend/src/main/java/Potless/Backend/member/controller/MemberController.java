package Potless.Backend.member.controller;

import Potless.Backend.global.format.code.ApiResponse;
import Potless.Backend.global.format.response.ResponseCode;
import Potless.Backend.global.jwt.service.TokenService;
import Potless.Backend.member.dto.EmailCheckRequestDto;
import Potless.Backend.member.dto.EmailValidationRequestDto;
import Potless.Backend.member.dto.LoginRequestDto;
import Potless.Backend.member.dto.SignupRequestDto;
import Potless.Backend.member.service.MailService;
import Potless.Backend.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final TokenService tokenService;
    private final MemberService memberService;
    private final ApiResponse response;
    private final MailService mailService;

    /* 일반 회원가입 */
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
    @PostMapping("/email/verify")
    public ResponseEntity<?> emailVerify(@Valid @RequestBody EmailCheckRequestDto requestDto,
                                         BindingResult bindingResult,
                                         HttpServletResponse servletResponse) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        return response.success(ResponseCode.EMAIL_VERIFIED_SUCCESS,
                mailService.confirmAuthCode(requestDto.getEmail(), requestDto.getAuthNum(), servletResponse));
    }

   /* 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto requestDto,
                                   BindingResult bindingResult,
                                   HttpServletResponse httpServletResponse) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        return response.success(ResponseCode.LOGIN_SUCCESS, memberService.login(requestDto, httpServletResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication, HttpServletResponse servletResponse) {
        return response.success(ResponseCode.LOGOUT_SUCCESS, memberService.logout(authentication.getName(), servletResponse));
    }

}
