package Potless.Backend.member.service;

import Potless.Backend.global.exception.member.DuplicateEmailException;
import Potless.Backend.global.exception.member.EmailNotFoundException;
import Potless.Backend.global.exception.member.InvalidLoginAttemptException;
import Potless.Backend.global.exception.member.PasswordMismatchException;
import Potless.Backend.global.jwt.TokenInfo;
import Potless.Backend.global.jwt.provider.TokenProvider;
import Potless.Backend.global.jwt.repository.RefreshTokenRepository;
import Potless.Backend.global.jwt.service.TokenService;
import Potless.Backend.global.util.CookieUtil;
import Potless.Backend.member.dto.LoginRequestDto;
import Potless.Backend.member.dto.LoginResponseDto;
import Potless.Backend.member.dto.MemberInfo;
import Potless.Backend.member.dto.SignupRequestDto;
import Potless.Backend.member.entity.MemberEntity;
import Potless.Backend.member.repository.member.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final CookieUtil cookieUtil;

    @Transactional
    public Long signup(SignupRequestDto requestDto){

        /* 비밀번호 불일치 */
        checkPasswordConfirmation(requestDto.getPassword(), requestDto.getPasswordConfirm());

        /* 이메일 중복 검증 */
        memberRepository.searchByEmail(requestDto.getEmail())
                .ifPresent(memberEntity -> { throw new DuplicateEmailException();}
        );

        MemberEntity newMember = MemberEntity.of(requestDto, passwordEncoder.encode(requestDto.getPassword()));
        memberRepository.save(newMember);

        return newMember.getId();
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse response) {
        log.info("event=LoginAttempt, email={}", requestDto.getEmail());

        MemberEntity member = memberRepository.searchByEmail(requestDto.getEmail())
                .orElseThrow(EmailNotFoundException::new);

        isPasswordMatchingWithEncoded(requestDto.getPassword(), member.getPassword());
        removeOldRefreshToken(requestDto, member);

        TokenInfo tokenInfo = tokenProvider.generateTokenInfo(member.getEmail());
        tokenService.saveToken(tokenInfo);
        cookieUtil.addCookie("RefreshToken", tokenInfo.getRefreshToken(), tokenProvider.getREFRESH_TOKEN_TIME(), response);

        return LoginResponseDto.builder()
                               .token(tokenInfo.getAccessToken())
                               .memberInfo(MemberInfo.builder()
                                                      .Id(member.getId())
                                                      .memberName(member.getMemberName())
                                                      .role(member.getRole())
                                                      .email(member.getEmail())
                                                      .phone(member.getPhone())
                                                      .region(member.getRegion())
                                                      .build())
                               .build();
    }

    @Transactional
    public String logout(String email, HttpServletResponse servletResponse) {
        cookieUtil.removeCookie("RefreshToken", servletResponse);
        refreshTokenRepository.findById(email)
                .ifPresent(refreshTokenRepository::delete);
        return email;
    }




    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkPasswordConfirmation(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new PasswordMismatchException();
        }
    }

    private void isPasswordMatchingWithEncoded(String input, String encoded) {
        if (!passwordEncoder.matches(input, encoded)) {
            throw new InvalidLoginAttemptException();
        }
    }

    private void removeOldRefreshToken(LoginRequestDto requestDto, MemberEntity member) {
        refreshTokenRepository.findById(member.getEmail())
                .ifPresent(refreshTokenRepository::delete);
        log.info("event=DeleteExistingRefreshToken, email={}", requestDto.getEmail());
    }
}
