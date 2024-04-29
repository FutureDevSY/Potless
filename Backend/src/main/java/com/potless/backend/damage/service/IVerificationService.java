package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;

import java.util.List;

public interface IVerificationService {

    boolean verificationDamage(List<DamageResponseDTO> damageVerification);
}
