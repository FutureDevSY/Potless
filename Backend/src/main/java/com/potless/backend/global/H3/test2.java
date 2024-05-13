//package com.potless.backend.global.H3;
//
//import com.uber.h3core.H3Core;
//import com.uber.h3core.util.LatLng;
//
//import java.io.IOException;
//import java.util.*;
//
//public class test2 {
//    public static void main(String[] args) throws IOException {
//        H3Core h3 = H3Core.newInstance();
//
//        LatLng[] daejeonLatLng = {
//                new LatLng(36.3274349, 127.2599176),
//                new LatLng(36.2982226, 127.2594992),
//                new LatLng(36.2970086, 127.2592803),
//                new LatLng(36.2959018, 127.2591087),
//                new LatLng(36.2960366, 127.2575594),
//                new LatLng(36.2965088, 127.2561069),
//                new LatLng(36.2968806, 127.2553986),
//                new LatLng(36.2972334, 127.2548236),
//                new LatLng(36.2964915, 127.2527551),
//                new LatLng(36.228145, 127.2934233),
//                new LatLng(36.2102967, 127.3523981),
//                new LatLng(36.1982966, 127.4501392),
//                new LatLng(127.5018726, 36.3311349),
//                new LatLng(127.2599176, 36.3274349)
//        };
//
//        Set<Long> deajeonH3Index = new HashSet<>();
//
//        List<Long> list = new ArrayList<>();
//        if (daejeonLatLng.length > 0) {
//            LatLng preCoord = daejeonLatLng[0];
//
//            for (int i = 1; i < daejeonLatLng.length; i++) {
//                LatLng curCoord = daejeonLatLng[i];
//
//                long preIndex = h3.latLngToCell(preCoord.lat, preCoord.lng, 15);
//                long curIndex = h3.latLngToCell(curCoord.lat, curCoord.lng, 15);
//
//                list = h3.gridPathCells(preIndex, curIndex);
//
//                for (int j = 0; j < list.size(); j++) {
//                    deajeonH3Index.add(list.get(j));
//                }
//                list.clear();
//                preCoord = curCoord;
//            }
//        }
//    }
//
//    public
//
//
//    public static Set<Long> bfs(double Lat, double Lng, Set<Long> set) throws IOException {
//        H3Core h3 = H3Core.newInstance();
////        double Lat = 127.3849016;
////        double Lng = 36.3497007;
//
//
//        long startLndex = h3.latLngToCell(Lat, Lng, 8);
//        set.add(startLndex);
//
//        Queue<Long> queue = new LinkedList<>();
//        queue.add(startLndex);
//
//        List<Long> list = new ArrayList<>();
//        while (!queue.isEmpty()) {
//            Long index = queue.poll();
//
//            list = h3.gridDisk(index,1);
//            for (Long i : list) {
//                if (set.contains(i)) {
//                    continue;
//                }
//                set.add(i);
//                queue.add(i);
//            }
//            list.clear();
//        }
//        return set;
//    }
//}
