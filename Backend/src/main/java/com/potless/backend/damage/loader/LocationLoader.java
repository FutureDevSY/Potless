package com.potless.backend.damage.loader;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.area.LocationEntity;
import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.damage.repository.LocationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
@RequiredArgsConstructor
public class LocationLoader implements CommandLineRunner {

    private static final Map<String, List<String>> areaDongMap = new HashMap<>();

    static {
        areaDongMap.put("동구", Arrays.asList("중앙동", "신인동", "효동", "판암1동", "판암2동", "용운동", "대동", "자양동", "가양1동", "가양2동", "용전동", "성남동", "홍도동", "삼성동", "대청동", "산내동"));
        areaDongMap.put("중구", Arrays.asList("은행선화동", "목동", "중촌동", "대흥동", "문창동", "석교동", "대사동", "부사동", "용두동", "오류동", "태평1동", "태평2동", "유천1동", "유천2동", "문화1동", "문화2동", "산성동"));
        areaDongMap.put("서구", Arrays.asList("복수동", "도마1동", "도마2동", "정림동", "변동", "괴정동", "가장동", "내동", "가수원동", "도안동", "관저1동", "관저2동", "기성동", "용문동", "탄방동", "둔산1동", "둔산2동", "둔산3동", "갈마1동", "갈마2동", "월평1동", "월평2동", "월평3동", "만년동"));
        areaDongMap.put("유성구", Arrays.asList("진잠동", "상대동", "원신흥동", "학하동", "원신흥동", "온천1동", "온천2동", "노은1동", "노은2동", "노은3동", "신성동", "전민동", "관평동", "구즉동"));
        areaDongMap.put("대덕구", Arrays.asList("오정동", "대화동", "회덕동", "비래동", "송촌동", "중리동", "법1동", "법2동", "신탄진동", "석봉동", "덕암동", "목상동"));
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
