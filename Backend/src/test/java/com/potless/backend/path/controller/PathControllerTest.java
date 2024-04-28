package com.potless.backend.path.controller;

import com.potless.backend.common.RestDocsSupport;
import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.path.dto.GetOptimalPathRequest;
import com.potless.backend.path.dto.KakaoWaypointResponse;
import com.potless.backend.path.dto.Location;
import com.potless.backend.path.service.PathService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PathControllerTest extends RestDocsSupport {

    private final PathService pathService = mock(PathService.class);
    private final ApiResponse response = new ApiResponse();

    @Override
    protected Object initController() {
        return new PathController(pathService, response);
    }

    @DisplayName("최적 경로 조회하기")
    @Test
    void 최적_경로_조회하기() throws Exception {

        GetOptimalPathRequest getOptimalPathRequest = GetOptimalPathRequest.builder()
                .origin(Location.builder()
                        .x(127.45480511466569)
                        .y(36.31181289054173)
                        .build())
                .potholeList(List.of(
                        Location.builder()
                                .x(127.45528340487891)
                                .y(36.30981045406812)
                                .build()
                ))
                .build();

        KakaoWaypointResponse kakaoWaypointResponse = KakaoWaypointResponse.builder()
                .transId("018f23d396b277feb14419a08a87b2db")
                .routes(List.of(
                        KakaoWaypointResponse.Route.builder()
                                .resultCode(0)
                                .resultMsg("길찾기 성공")
                                .summary(KakaoWaypointResponse.Summary.builder()
                                        .origin(Location.builder()
                                                .x(127.45480459199737)
                                                .y(36.31180980422176)
                                                .build())
                                        .destination(Location.builder()
                                                .x(127.45480459199737)
                                                .y(36.31180980422176)
                                                .build())
                                        .waypoints(List.of(Location.builder()
                                                .x(127.45527512572122)
                                                .y(36.30980214310938)
                                                .build()))
                                        .priority("RECOMMEND")
                                        .bound(KakaoWaypointResponse.Bound.builder()
                                                .minX(127.45431793973266)
                                                .minY(36.309734690004134)
                                                .maxX(127.45715205196082)
                                                .maxY(36.31212691406215)
                                                .build())
                                        .fare(KakaoWaypointResponse.Summary.Fare.builder()
                                                .taxi(4300)
                                                .toll(0)
                                                .build())
                                        .distance(936)
                                        .duration(320)
                                        .build())
                                .sections(List.of(KakaoWaypointResponse.Section.builder()
                                        .distance(395)
                                        .duration(109)
                                        .bound(KakaoWaypointResponse.Bound.builder()
                                                .minX(127.4552973960855)
                                                .minY(36.30980224457682)
                                                .maxX(127.45553428740136)
                                                .maxY(36.31211459670025)
                                                .build())
                                        .roads(List.of(KakaoWaypointResponse.Section.Road.builder()
                                                .name("")
                                                .distance(26)
                                                .duration(6)
                                                .trafficSpeed(16.0)
                                                .trafficState(0)
                                                .vertexes(List.of(new Double[]{127.45457049611794,
                                                        36.311844786770486,
                                                        127.45462454157789,
                                                        36.3120793604265}))
                                                .build()))
                                        .guides(List.of(KakaoWaypointResponse.Section.Guide.builder()
                                                .name("출발지")
                                                .x(127.45457049611794)
                                                .y(36.311844786770486)
                                                .distance(0)
                                                .duration(0)
                                                .type(100)
                                                .guidance("출발지")
                                                .roadIndex(0)
                                                .build()))
                                        .build()))
                                .build()
                ))
                .build();
        given(pathService.getOptimalPath(any(GetOptimalPathRequest.class)))
                .willReturn(kakaoWaypointResponse);

        String requestJson = "{\"origin\":{\"x\":127.45480511466569,\"y\":36.31181289054173},\"potholeList\":[{\"x\":127.45528340487891,\"y\":36.30981045406812}]}";
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("origin", "출발지");
        requestMap.put("potholeList", "작업할 포트홀 리스트");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/path/optimal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-optimal-path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(requestMap),
                        relaxedResponseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태 (SUCCESS, FAIL, ERROR 중 하나)"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.trans_id").type(JsonFieldType.STRING).description("결과 ID"),
                                fieldWithPath("data.routes[]").type(JsonFieldType.ARRAY).description("경로 정보"),
                                fieldWithPath("data.routes[].result_code").type(JsonFieldType.NUMBER).description("경로 탐색 결과 코드"),
                                fieldWithPath("data.routes[].result_msg").type(JsonFieldType.STRING).description("경로 탐색 결과 메시지"),
                                fieldWithPath("data.routes[].summary").type(JsonFieldType.OBJECT).description("경로 요약 정보"),
                                fieldWithPath("data.routes[].summary.origin").type(JsonFieldType.OBJECT).description("출발지 정보"),
                                fieldWithPath("data.routes[].summary.origin.x").type(JsonFieldType.NUMBER).description("X 좌표(경도)"),
                                fieldWithPath("data.routes[].summary.origin.y").type(JsonFieldType.NUMBER).description("Y 좌표(위도)"),
                                fieldWithPath("data.routes[].summary.destination").type(JsonFieldType.OBJECT).description("목적지 정보"),
                                fieldWithPath("data.routes[].summary.destination.x").type(JsonFieldType.NUMBER).description("X 좌표(경도)"),
                                fieldWithPath("data.routes[].summary.destination.y").type(JsonFieldType.NUMBER).description("Y 좌표(위도)"),
                                fieldWithPath("data.routes[].summary.waypoints[]").type(JsonFieldType.ARRAY).description("경유지 정보"),
                                fieldWithPath("data.routes[].summary.waypoints[].x").type(JsonFieldType.NUMBER).description("X 좌표(경도)"),
                                fieldWithPath("data.routes[].summary.waypoints[].y").type(JsonFieldType.NUMBER).description("Y 좌표(위도)"),
                                fieldWithPath("data.routes[].summary.priority").type(JsonFieldType.STRING).description("경로 탐색 우선순위 옵션"),
                                fieldWithPath("data.routes[].summary.bound").type(JsonFieldType.OBJECT).description("모든 경로를 포함하는 사각형의 바운딩 박스(Bounding box)"),
                                fieldWithPath("data.routes[].summary.bound.min_x").type(JsonFieldType.NUMBER).description("바운딩 박스 왼쪽 하단의 X 좌표"),
                                fieldWithPath("data.routes[].summary.bound.min_y").type(JsonFieldType.NUMBER).description("바운딩 박스 왼쪽 하단의 Y 좌표"),
                                fieldWithPath("data.routes[].summary.bound.max_x").type(JsonFieldType.NUMBER).description("바운딩 박스 오른쪽 상단의 X 좌표"),
                                fieldWithPath("data.routes[].summary.bound.max_y").type(JsonFieldType.NUMBER).description("바운딩 박스 오른쪽 상단의 Y 좌표"),
                                fieldWithPath("data.routes[].summary.fare").type(JsonFieldType.OBJECT).description("요금 정보"),
                                fieldWithPath("data.routes[].summary.fare.taxi").type(JsonFieldType.NUMBER).description("택시 요금(원)"),
                                fieldWithPath("data.routes[].summary.fare.toll").type(JsonFieldType.NUMBER).description("통행 요금(원)"),
                                fieldWithPath("data.routes[].summary.distance").type(JsonFieldType.NUMBER).description("전체 검색 결과 거리(미터)"),
                                fieldWithPath("data.routes[].summary.duration").type(JsonFieldType.NUMBER).description("목적지까지 소요 시간(초)"),
                                fieldWithPath("data.routes[].sections[]").type(JsonFieldType.ARRAY).description("구간별 경로 정보"),
                                fieldWithPath("data.routes[].sections[].distance").type(JsonFieldType.NUMBER).description("섹션 거리(미터)"),
                                fieldWithPath("data.routes[].sections[].duration").type(JsonFieldType.NUMBER).description("전체 검색 결과 이동 시간(초)"),
                                fieldWithPath("data.routes[].sections[].bound").type(JsonFieldType.OBJECT).description("모든 경로를 포함하는 사각형의 바운딩 박스(Bounding box)"),
                                fieldWithPath("data.routes[].sections[].bound.min_x").type(JsonFieldType.NUMBER).description("바운딩 박스 왼쪽 하단의 X 좌표"),
                                fieldWithPath("data.routes[].sections[].bound.min_y").type(JsonFieldType.NUMBER).description("바운딩 박스 왼쪽 하단의 Y 좌표"),
                                fieldWithPath("data.routes[].sections[].bound.max_x").type(JsonFieldType.NUMBER).description("바운딩 박스 오른쪽 상단의 X 좌표"),
                                fieldWithPath("data.routes[].sections[].bound.max_y").type(JsonFieldType.NUMBER).description("바운딩 박스 오른쪽 상단의 Y 좌표"),
                                fieldWithPath("data.routes[].sections[].roads[]").type(JsonFieldType.ARRAY).description("도로 정보"),
                                fieldWithPath("data.routes[].sections[].roads[].name").type(JsonFieldType.STRING).description("도로명"),
                                fieldWithPath("data.routes[].sections[].roads[].distance").type(JsonFieldType.NUMBER).description("도로 길이(미터)"),
                                fieldWithPath("data.routes[].sections[].roads[].duration").type(JsonFieldType.NUMBER).description("예상 이동 시간(초)"),
                                fieldWithPath("data.routes[].sections[].roads[].traffic_speed").type(JsonFieldType.NUMBER).description("현재 교통 정보 속도(km/h)"),
                                fieldWithPath("data.routes[].sections[].roads[].traffic_state").type(JsonFieldType.NUMBER).description("현재 교통 정보 상태"),
                                fieldWithPath("data.routes[].sections[].roads[].vertexes[]").type(JsonFieldType.ARRAY).description("X, Y 좌표로 구성된 1차원 배열"),
                                fieldWithPath("data.routes[].sections[].guides[]").type(JsonFieldType.ARRAY).description("안내 정보"),
                                fieldWithPath("data.routes[].sections[].guides[].name").type(JsonFieldType.STRING).description("명칭"),
                                fieldWithPath("data.routes[].sections[].guides[].x").type(JsonFieldType.NUMBER).description("X 좌표(경도)"),
                                fieldWithPath("data.routes[].sections[].guides[].y").type(JsonFieldType.NUMBER).description("Y 좌표(위도)"),
                                fieldWithPath("data.routes[].sections[].guides[].distance").type(JsonFieldType.NUMBER).description("이전 가이드 지점부터 현재 가이드 지점까지 거리(미터)"),
                                fieldWithPath("data.routes[].sections[].guides[].duration").type(JsonFieldType.NUMBER).description("이전 가이드 지점부터 현재 가이드 지점까지 시간(초)"),
                                fieldWithPath("data.routes[].sections[].guides[].type").type(JsonFieldType.NUMBER).description("안내 타입"),
                                fieldWithPath("data.routes[].sections[].guides[].guidance").type(JsonFieldType.STRING).description("안내 문구"),
                                fieldWithPath("data.routes[].sections[].guides[].road_index").type(JsonFieldType.NUMBER).description("현재 가이드에 대한 링크 인덱스")
                        )
                ));


    }

}