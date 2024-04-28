package com.potless.backend.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potless.backend.common.RestDocsSupport;
import com.potless.backend.damage.controller.DamageController;
import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.jwt.provider.TokenProvider;
import com.potless.backend.global.jwt.service.TokenService;
import com.potless.backend.member.dto.LoginRequestDto;
import com.potless.backend.member.dto.LoginResponseDto;
import com.potless.backend.member.dto.MemberInfo;
import com.potless.backend.member.service.MailService;
import com.potless.backend.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class MemberControllerTest extends RestDocsSupport {

    private final MemberService memberService = mock(MemberService.class);
    private final TokenService tokenService = mock(TokenService.class);
    private final MailService mailService = mock(MailService.class);
    private final ApiResponse apiResponse = new ApiResponse();

    private ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper를 직접 인스턴스화

    @Override
    protected Object initController() {
        return new MemberController(tokenService, memberService, apiResponse, mailService);
    }

    @DisplayName("웹 로그인")
    @Test
    void 웹_사용자_로그인() throws Exception {

        //given
        // 유효한 요청 데이터
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                                                         .email("test1234@gmail.com")
                                                         .password("test1234")
                                                         .build();
        //지역이름 추가?

        MemberInfo memberInfo = MemberInfo.builder()
                                          .memberName("김관리자")
                                          .Id(1L)
                                          .phone("010-1234-1234")
                                          .role(0)
                                          .region(1)
                                          .email("test1234@gmail.com")
                                          .build();

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                                                            .token("tokensample")
                                                            .memberInfo(memberInfo)

                                                            .build();
        given(memberService.login(any(LoginRequestDto.class), any(HttpServletResponse.class), eq(0)))
                .willReturn(loginResponseDto);


        // 서비스 로직 모의 설정
        //when

//        int identify = 0;
//        when(memberService.login(any(LoginRequestDto.class), any(HttpServletResponse.class), identify, eq(0)))
//                .thenReturn(LoginResponseDto.builder()
//                        .token(TokenProvider.class.generateTokenInfo(loginRequestDto.getEmail(), identify))
//                                            .build());
        String jsonContent = objectMapper.writeValueAsString(loginRequestDto);

        // API 요청 및 응답 검증
        mockMvc.perform(
                post("/api/member/login-web")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))

                    .andDo(print())
                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.token").exists())
                    .andDo(document("login-web-valid",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("email").description("사용자 이메일(아이디)"),
                                    fieldWithPath("password").description("사용자 비밀번호")
                            ),
                            responseFields(
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태 (SUCCESS, FAIL, ERROR 중 하나)"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                    fieldWithPath("data.token").type(JsonFieldType.STRING).description("인증 JWT 토큰"),
                                    fieldWithPath("data.memberInfo.id").type(JsonFieldType.NUMBER).description("회원 Id"),
                                    fieldWithPath("data.memberInfo.memberName").type(JsonFieldType.STRING).description("회원 이름"),
                                    fieldWithPath("data.memberInfo.role").type(JsonFieldType.NUMBER).description("회원 직책"),
                                    fieldWithPath("data.memberInfo.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                    fieldWithPath("data.memberInfo.phone").type(JsonFieldType.STRING).description("회원 연락처"),
                                    fieldWithPath("data.memberInfo.region").type(JsonFieldType.NUMBER).description("회원 담당지역")
                            )));

    }

}
