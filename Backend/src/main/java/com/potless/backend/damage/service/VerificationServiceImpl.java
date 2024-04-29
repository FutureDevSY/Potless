package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationServiceImpl implements IVerificationService {

    @Override
    public boolean verificationDamage(List<DamageResponseDTO> damageVerification) {
        // 리스트가 비어있는 경우 바로 true 반환
        if (damageVerification.isEmpty()) {
            return true;
        }

        // 추가 검증 로직
        // 예를 들어, 특정 조건을 만족하는 경우 true를 반환할 수 있음
//        for (DamageResponseDTO damage : damageVerification) {
//            // 이곳에 조건을 추가하고, 조건이 만족하면 true 반환
//            // 예시: if (damage.getStatus() == Status.PENDING) {
//            //    return true;
//            // }
//        }

        // 일단 검증 없이 로직 구현 할거니까 true
        return true;
    }
}
