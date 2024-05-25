package com.potless.backend.damage.loader;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class AreaLoader implements CommandLineRunner {

    private final AreaRepository areaRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<String> areas = Arrays.asList("대덕구", "동구", "중구", "유성구", "서구", "기타", "강남구");
        areas.forEach(areaName -> {
            areaRepository.findByAreaGu(areaName).ifPresentOrElse(
                    area -> {
                        // 이미 존재하는 구는 건너뛰기
                        System.out.println(areaName + " already exists.");
                    },
                    () -> {
                        // 존재하지 않는 구는 추가
                        AreaEntity newArea = AreaEntity.builder()
                                .areaGu(areaName)
                                .build();
                        areaRepository.save(newArea);
                        System.out.println(areaName + " added.");
                    }
            );
        });
    }
}
