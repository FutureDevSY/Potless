//package com.potless.backend.damage.controller;
//
//import com.potless.backend.aws.service.AwsService;
//import com.potless.backend.common.RestDocsSupport;
//import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
//import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
//import com.potless.backend.damage.dto.controller.response.ImagesResponseDTO;
//import com.potless.backend.damage.entity.enums.Status;
//import com.potless.backend.damage.service.IDamageService;
//import com.potless.backend.damage.service.IVerificationService;
//import com.potless.backend.damage.service.KakaoService;
//import com.potless.backend.global.format.code.ApiResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.payload.JsonFieldType;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.mockito.BDDMockito.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Slf4j
//class DamageControllerTest extends RestDocsSupport {
//
//    private final IDamageService iDamageService = mock(IDamageService.class);
//    private final KakaoService kakaoService = mock(KakaoService.class);
//    private final IVerificationService iVerificationService = mock(IVerificationService.class);
//    private final ApiResponse response = new ApiResponse();
//    private final AwsService awsService = mock(AwsService.class);
//
//    @Override
//    protected Object initController() {
//        return new DamageController(iDamageService, kakaoService, response, iVerificationService, awsService);
//    }
//
//    @DisplayName("도로 파손 리스트 가져오기")
//    @Test
//    void 도로_파손_리스트_가져오기() throws Exception {
//        // given
//        DamageSearchRequestDTO damageSearchRequestDTO = DamageSearchRequestDTO.builder()
//                .start(LocalDate.of(2024, 3, 24))
//                .end(LocalDate.of(2024, 4, 24))
//                .type("POTHOLE")
//                .status(Status.작업전)
//                .severity(0)
//                .area("대덕구")
//                .searchWord("송촌동")
//                .build();
//
//        Pageable pageable = PageRequest.of(0, 10);
//
//        List<DamageResponseDTO> content = List.of(
//                DamageResponseDTO.builder()
//                        .id(1L)
//                        .severity(0)
//                        .dirX(127.423084873712)
//                        .dirY(37.0789561558879)
//                        .address("경기도 안성시 죽산면 죽산초교길 69-4")
//                        .width(5.5)
//                        .status(Status.작업전)
//                        .area("안성시")
//                        .location("죽산면")
//                        .imagesResponseDTOS(List.of(
//                                ImagesResponseDTO.builder()
//                                        .url("asdfasdfxczvlkonweo")
//                                        .id(1L)
//                                        .order(1)
//                                        .build(),
//
//                                ImagesResponseDTO.builder()
//                                        .url("ASDFZCXVWERFASDFCXZVWEF")
//                                        .id(2L)
//                                        .order(2)
//                                        .build()
//                        ))
//                        .dtype("POTHOLE")
//                        .build(),
//
//                DamageResponseDTO.builder()
//                        .id(1L)
//                        .severity(0)
//                        .dirX(127.423084873712)
//                        .dirY(37.0789561558879)
//                        .address("경기도 안성시 죽산면 죽산초교길 69-4")
//                        .width(5.5)
//                        .status(Status.작업전)
//                        .area("안성시")
//                        .location("죽산면")
//                        .imagesResponseDTOS(List.of(
//                                ImagesResponseDTO.builder()
//                                        .url("asdfasdfxczvlkonweo")
//                                        .id(1L)
//                                        .order(1)
//                                        .build(),
//
//                                ImagesResponseDTO.builder()
//                                        .url("ASDFZCXVWERFASDFCXZVWEF")
//                                        .id(2L)
//                                        .order(2)
//                                        .build()
//                        ))
//                        .dtype("POTHOLE")
//                        .build(),
//
//                DamageResponseDTO.builder()
//                        .id(1L)
//                        .severity(0)
//                        .dirX(127.4234546242534)
//                        .dirY(37.078243523468879)
//                        .address("대전 대덕구 동춘당로")
//                        .width(5.5)
//                        .status(Status.작업전)
//                        .area("대전시")
//                        .location("송촌동")
//                        .imagesResponseDTOS(List.of(
//                                ImagesResponseDTO.builder()
//                                        .url("asdfasdfxczvlkonweo")
//                                        .id(1L)
//                                        .order(1)
//                                        .build(),
//
//                                ImagesResponseDTO.builder()
//                                        .url("ASDFZCXVWERFASDFCXZVWEF")
//                                        .id(2L)
//                                        .order(2)
//                                        .build()
//                        ))
//                        .dtype("POTHOLE")
//                        .build(),
//
//                DamageResponseDTO.builder()
//                        .id(1L)
//                        .severity(0)
//                        .dirX(127.423084873712)
//                        .dirY(37.0789561558879)
//                        .address("경기도 안성시 죽산면 죽산초교길 69-4")
//                        .width(5.5)
//                        .status(Status.작업전)
//                        .area("안성시")
//                        .location("죽산면")
//                        .imagesResponseDTOS(List.of(
//                                ImagesResponseDTO.builder()
//                                        .url("asdfasdfxczvlkonweo")
//                                        .id(1L)
//                                        .order(1)
//                                        .build(),
//
//                                ImagesResponseDTO.builder()
//                                        .url("ASDFZCXVWERFASDFCXZVWEF")
//                                        .id(2L)
//                                        .order(2)
//                                        .build()
//                        ))
//                        .dtype("POTHOLE")
//                        .build()
//
//        );
//
//        Page<DamageResponseDTO> page = new PageImpl<>(content, pageable, content.size());
//        given(iDamageService.getDamages(any(DamageSearchRequestDTO.class), any(Pageable.class)))
//                .willReturn(page);
//
//        // when
//        // then
//        mockMvc.perform(
//                        get("/api/damage")
//                                .param("start", "2024-03-24")
//                                .param("end", "2024-04-24")
//                                .param("type", "POTHOLE")
//                                .param("status", "작업전")
//                                .param("severity", "0")
//                                .param("area", "대덕구")
//                                .param("searchWord", "송촌동")
//                                .param("page", "0")
//                                .param("size", "10")
//                                .contentType(MediaType.APPLICATION_JSON))
//
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("get-damage-list",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        queryParameters(
//                                parameterWithName("start").description("시작 날짜")
//                                        .optional(),
//                                parameterWithName("end").description("종료 날짜")
//                                        .optional(),
//                                parameterWithName("type").description("포트홀 및 도로 파손 타입 조회")
//                                        .optional(),
//                                parameterWithName("status").description("작업 진행 상태")
//                                        .optional(),
//                                parameterWithName("severity").description("위험도")
//                                        .optional(),
//                                parameterWithName("area").description("대전 구")
//                                        .optional(),
//                                parameterWithName("searchWord").description("대전 동, 도로명")
//                                        .optional(),
//                                parameterWithName("page").description("페이지 시작 위치")
//                                        .optional(),
//                                parameterWithName("size").description("페이지 사이즈")
//                                        .optional()
//                        ),
//                        responseFields(
//                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태 (SUCCESS, FAIL, ERROR 중 하나)"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
//                                fieldWithPath("data.content[]").type(JsonFieldType.ARRAY).description("도로 파손 정보 목록"),
//                                fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER).description("도로 파손 식별자"),
//                                fieldWithPath("data.content[].severity").type(JsonFieldType.NUMBER).description("위험도"),
//                                fieldWithPath("data.content[].dirX").type(JsonFieldType.NUMBER).description("경도"),
//                                fieldWithPath("data.content[].dirY").type(JsonFieldType.NUMBER).description("위도"),
//                                fieldWithPath("data.content[].address").type(JsonFieldType.STRING).description("주소"),
//                                fieldWithPath("data.content[].roadName").type(JsonFieldType.STRING).description("도로 이름"),
//                                fieldWithPath("data.content[].width").type(JsonFieldType.NUMBER).description("도로 폭"),
//                                fieldWithPath("data.content[].status").type(JsonFieldType.STRING).description("현재 상태"),
//                                fieldWithPath("data.content[].area").type(JsonFieldType.STRING).description("지역"),
//                                fieldWithPath("data.content[].location").type(JsonFieldType.STRING).description("세부 지역"),
//                                fieldWithPath("data.content[].imagesResponseDTOS[].id").type(JsonFieldType.NUMBER).description("이미지 식별자"),
//                                fieldWithPath("data.content[].imagesResponseDTOS[].url").type(JsonFieldType.STRING).description("이미지 URL"),
//                                fieldWithPath("data.content[].imagesResponseDTOS[].order").type(JsonFieldType.NUMBER).description("이미지 순서"),
//                                fieldWithPath("data.content[].dtype").type(JsonFieldType.STRING).description("파손 타입"),
//                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이지 정보"),
//                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
//                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
//                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("현재 페이지의 첫 번째 요소에 대한 오프셋"),
//                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 처리 여부"),
//                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징 미처리 여부"),
//                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 적용 여부"),
//                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬이 적용되었는지 여부"),
//                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬이 적용되지 않았는지 여부"),
//                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
//                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
//                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수"),
//                                fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
//                                fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
//                                fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬"),
//                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 적용 여부"),
//                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬이 적용되었는지 여부"),
//                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬이 적용되지 않았는지 여부"),
//                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 번째 페이지 여부"),
//                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 요소 수"),
//                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("현재 페이지가 비어 있는지 여부")
//                        )
//                ));
//    }
//
//}