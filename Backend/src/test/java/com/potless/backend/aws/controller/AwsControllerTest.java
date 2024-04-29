package com.potless.backend.aws.controller;

import com.potless.backend.aws.service.AwsService;
import com.potless.backend.common.RestDocsSupport;
import com.potless.backend.global.format.code.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AwsControllerTest extends RestDocsSupport {

    private final AwsService awsService = mock(AwsService.class);
    private final ApiResponse response = new ApiResponse();


    @Override
    protected Object initController() {
        return new AwsController(awsService, response);
    }

//    @DisplayName("S3 검증 전 폴더에 이미지 넣고 저장 링크 반환하기")
//    @Test
//    void S3_검증전_폴더에_이미지_넣고_저장_링크_반환하기() throws Exception {
//        // given
//        MockMultipartFile mockFile = new MockMultipartFile(
//                "file",
//                "고양이.jpg",
//                MediaType.IMAGE_JPEG_VALUE,
//                "test data".getBytes());
//
//        String fileUrl = "https://mine702-amazon-s3.s3.ap-northeast-2.amazonaws.com/%EA%B2%80%EC%A6%9D%EC%A0%84/1714102488134_%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C%20%282%29.jpg";
//        given(awsService.uploadFileToS3(any(MultipartFile.class)))
//                .willReturn(fileUrl);
//        // when
//        // then
//
//        mockMvc.perform(
//                        multipart("/api/aws/upload")
//                                .file(mockFile)
//                                .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("upload_to_s3",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestParts(
//                                partWithName("file").description("업로드할 파일")
//                        ),
//                        responseFields(
//                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태 (SUCCESS, FAIL, ERROR 중 하나)"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.STRING).description("응답 데이터")
//                        )
//                ));
//    }

}