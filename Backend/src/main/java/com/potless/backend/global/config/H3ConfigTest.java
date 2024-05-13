//package com.potless.backend.global.H3;
//
//import com.uber.h3core.H3Core;
//import com.uber.h3core.util.LatLng;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//public class H3ConfigTest {
//
//    public static void main(String[] args) throws IOException {
//        H3Core h3 = H3Core.newInstance();
//
//        // 대전광역시 경계선 좌표를 사용하여 다각형을 정의합니다.
//        List<LatLng> daejeonBoundary = new ArrayList<>();
//        daejeonBoundary.add(new LatLng(36.3274349, 127.2599176));
//        daejeonBoundary.add(new LatLng(36.2982226, 127.2594992));
//        daejeonBoundary.add(new LatLng(36.2970086, 127.2592803));
//        // 추가적인 좌표들을 여기에 입력...
//        daejeonBoundary.add(new LatLng(127.2599176, 36.3274349)); // 잘못된 좌표 순서 수정
//
//        // 경계선을 기반으로 H3 셀 인덱스를 계산합니다.
//        List<Long> daejeonH3Index = h3.polygonToCells(daejeonBoundary, null,11);
//
//        // 결과 출력
//        System.out.println("H3 Indexes covering Daejeon:");
//        for (Long index : daejeonH3Index) {
//            System.out.println(index);
//        }
//    }
//}
