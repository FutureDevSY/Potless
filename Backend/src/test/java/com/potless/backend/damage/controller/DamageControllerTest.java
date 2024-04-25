package com.potless.backend.damage.controller;

import com.potless.backend.common.RestDocsSupport;
import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.controller.response.ImagesResponseDTO;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.service.IDamageService;
import com.potless.backend.damage.service.KakaoService;
import com.potless.backend.global.format.code.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DamageControllerTest extends RestDocsSupport {

    private final IDamageService iDamageService = mock(IDamageService.class);
    private final KakaoService kakaoService = mock(KakaoService.class);
    private final ApiResponse response = mock(ApiResponse.class);

    @Override
    protected Object initController() {
        return new DamageController(iDamageService, kakaoService, response);
    }


    @DisplayName("도로 파손 리스트 가져오기")
    @Test
    void 도로_파손_리스트_가져오기() throws Exception {

        // given
        DamageSearchRequestDTO damageSearchRequestDTO = DamageSearchRequestDTO.builder()
                .start(LocalDate.of(2024, 3, 24))
                .end(LocalDate.of(2024, 4, 24))
                .type("POTHOLE")
                .status(Status.작업전)
                .severity(0)
                .area("대덕구")
                .searchWord("송촌동")
                .build();

        Pageable pageable = PageRequest.of(0, 2);


        List<DamageResponseDTO> content = List.of(
                DamageResponseDTO.builder()
                        .id(1L)
                        .severity(0)
                        .dirX(127.423084873712)
                        .dirY(37.0789561558879)
                        .address("경기도 안성시 죽산면 죽산초교길 69-4")
                        .roadName("죽산초교길")
                        .width(5.5)
                        .status(Status.작업전)
                        .area("안성시")
                        .location("죽산면")
                        .imagesResponseDTOS(List.of(
                                ImagesResponseDTO.builder()
                                        .url("asdfasdfxczvlkonweo")
                                        .id(1L)
                                        .order(1)
                                        .build(),

                                ImagesResponseDTO.builder()
                                        .url("ASDFZCXVWERFASDFCXZVWEF")
                                        .id(2L)
                                        .order(2)
                                        .build()
                        ))
                        .dtype("POTHOLE")
                        .build(),

                DamageResponseDTO.builder()
                        .id(1L)
                        .severity(0)
                        .dirX(127.423084873712)
                        .dirY(37.0789561558879)
                        .address("경기도 안성시 죽산면 죽산초교길 69-4")
                        .roadName("죽산초교길")
                        .width(5.5)
                        .status(Status.작업전)
                        .area("안성시")
                        .location("죽산면")
                        .imagesResponseDTOS(List.of(
                                ImagesResponseDTO.builder()
                                        .url("asdfasdfxczvlkonweo")
                                        .id(1L)
                                        .order(1)
                                        .build(),

                                ImagesResponseDTO.builder()
                                        .url("ASDFZCXVWERFASDFCXZVWEF")
                                        .id(2L)
                                        .order(2)
                                        .build()
                        ))
                        .dtype("POTHOLE")
                        .build()

        );

        Page<DamageResponseDTO> damages = new PageImpl<>(content, pageable, content.size());

        given(iDamageService.getDamages(any(DamageSearchRequestDTO.class), any(Pageable.class)))
                .willReturn(damages);

        // when
        // then
        mockMvc.perform(
                        get("/api/damage")
                                .param("start", "2024-03-24")
                                .param("end", "2024-04-24")
                                .param("type", "POTHOLE")
                                .param("status", "작업전")
                                .param("severity", "0")
                                .param("area", "대덕구")
                                .param("searchWord", "송촌동")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-damage-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("start").description("시작 날짜")
                                        .optional(),
                                parameterWithName("end").description("종료 날짜")
                                        .optional()
                                , parameterWithName("type").description("포트홀 및 도로 파손 타입 조회")
                                        .optional()
                                , parameterWithName("status").description("작업 진행 상태")
                                        .optional(),
                                parameterWithName("severity").description("위험도")
                                        .optional(),
                                parameterWithName("area").description("대전 구")
                                        .optional(),
                                parameterWithName("searchWord").description("대전 동, 도로명")
                                        .optional(),
                                parameterWithName("page").description("페이지 시작 위치")
                                        .optional(),
                                parameterWithName("size").description("페이지 사이즈")
                                        .optional()
                        ),
                        responseFields(

                        )
                ));

    }


}