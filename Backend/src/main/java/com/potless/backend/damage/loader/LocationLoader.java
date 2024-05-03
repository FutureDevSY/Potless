package com.potless.backend.damage.loader;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.area.LocationEntity;
import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.damage.repository.LocationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
@RequiredArgsConstructor
@Order(2)
public class LocationLoader implements CommandLineRunner {

    private static final Map<String, List<String>> areaDongMap = new HashMap<>();

    static {
        areaDongMap.put("동구", Arrays.asList(
                "가양동", "가오동", "구도동", "낭월동", "내탑동", "대별동", "대성동", "마산동",
                "비룡동", "삼괴동", "삼성동", "삼정동", "성남동", "세천동", "소제동", "신상동",
                "신안동", "신촌동", "신흥동", "용계동", "용운동", "용전동", "사성동", "이사동",
                "자양동", "장척동", "주산동", "주촌동", "소호동", "신하동", "판암동", "하소동",
                "홍도동", "효평동", "상소동", "대동", "오동", "원동", "중동", "직동", "인동",
                "천동", "정동", "추동", "효동"
        ));
        areaDongMap.put("중구", Arrays.asList(
                "구완동", "대사동", "대흥동", "목달동", "무수동", "문창동", "문화동", "부사동",
                "사정동", "산성동", "석교동", "선화동", "안영동", "어남동", "오류동", "옥계동",
                "용두동", "유천동", "은행동", "정생동", "중촌동", "침산동", "태평동", "금동",
                "목동", "호동"
        ));
        areaDongMap.put("서구", Arrays.asList(
                "가장동", "갈마동", "관저동", "괴곡동", "괴정동", "도마동", "도안동", "둔산동",
                "만년동", "매로동", "복수동", "봉곡동", "산직동", "삼천동", "용문동", "용촌동",
                "우명동", "원정동", "월평동", "장안동", "정림동", "탄방동", "평촌동", "흑석동",
                "내동", "변동", "오동", "가수원동"
        ));
        areaDongMap.put("유성구", Arrays.asList(
                "가정동", "계산동", "구성동", "관평동", "교촌동", "구룡동", "구암동", "금고동",
                "금탄동", "노은동", "대정동", "덕명동", "덕진동", "도룡동", "둔곡동", "반석동",
                "문지동", "방현동", "복룡동", "봉명동", "봉산동", "상대동", "성북동", "송강동",
                "송정동", "수남동", "신봉동", "신성동", "안산동", "어은동", "외삼동", "용계동",
                "용산동", "원내동", "원촌동", "자운동", "장대동", "전민동", "지족동", "추목동",
                "탑림동", "하기동", "학하동", "화암동", "원신흥동", "갑동", "궁동", "대동",
                "방동", "세동", "신동", "장동", "죽동"
        ));
        areaDongMap.put("대덕구", Arrays.asList(
                "갈전동", "대화동", "덕암동", "목상동", "문평동", "미호동", "부수동", "비래동",
                "삼정동", "상서동", "석봉동", "송촌동", "연축동", "오정동", "신일동", "신대동",
                "용호동", "읍내동", "이현동", "중리동", "평촌동", "황호동", "법동", "장동",
                "와동", "신탄진동"
        ));
    }

    private final LocationRepository locationRepository;
    private final AreaRepository areaRepository;

    @Override
    public void run(String... args) throws Exception {
        areaDongMap.forEach((areaName, dongs) -> {
            AreaEntity areaEntity = areaRepository.findByAreaGu(areaName)
                    .orElseThrow(() -> new IllegalStateException(areaName + " AreaEntity가 존재하지 않습니다."));

            addLocationsToArea(areaEntity, dongs);
        });
    }

    private void addLocationsToArea(AreaEntity areaEntity, List<String> dongs) {
        dongs.forEach(dongName -> {
            LocationEntity locationEntity = locationRepository.findByLocationName(dongName)
                    .orElseGet(() -> {
                        LocationEntity newLocation = LocationEntity.builder()
                                .locationName(dongName)
                                .areaEntity(areaEntity)
                                .build();
                        return locationRepository.save(newLocation);
                    });
        });
    }
}
