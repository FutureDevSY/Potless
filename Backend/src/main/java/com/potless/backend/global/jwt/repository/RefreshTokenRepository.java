package com.potless.backend.global.jwt.repository;

import com.potless.backend.global.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
