//package com.potless.backend.hexagon.controller;
//
//import com.potless.backend.hexagon.dto.requestDto.HexagonRequestDto;
//import com.potless.backend.global.format.code.ApiResponse;
//import com.potless.backend.global.format.response.ResponseCode;
//import com.potless.backend.hexagon.service.HexagonService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.List;
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("api/hexagon")
//@Tag(name = "H3 컨트롤러", description = "H3 Controller API")
//public class HexagonController {
//    private final HexagonService hexagonService;
//    private final ApiResponse response;
//
//    @PostMapping
//    public ResponseEntity<?> duplCheck(
//            @RequestPart Double x,
//            @RequestPart Double y,
//            @RequestPart String dtype
//    ) {
//        boolean result = hexagonService.duplCheck(x,y,dtype);
//
//        if (result) {
//            return response.success(ResponseCode.DUPLICATION_TRUE, result);
//        } else {
//            return response.success(ResponseCode.DUPLICATION_FALSE, result);
//        }
//    }
//
//}
