package com.potless.backend.global.jwt.provider;

import com.potless.backend.global.jwt.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class TokenProvider {

    @Getter
    private final int ACCESS_TOKEN_TIME;

    @Getter
    private final int REFRESH_TOKEN_TIME_WEB;

    @Getter
    private final int REFRESH_TOKEN_TIME_APP;

    private final String SECRET_KEY;

    private final Key key;

    public TokenProvider(@Value("${jwt.ACCESS_TIME}") final int ACCESS_TOKEN_TIME,
                         @Value("${jwt.REFRESH_TIME_WEB}") final int REFRESH_TOKEN_TIME_WEB,
                         @Value("${jwt.REFRESH_TIME_APP}") final int REFRESH_TOKEN_TIME_APP,
                         @Value("${jwt.SECRET_KEY}") final String SECRET_KEY) {
        this.ACCESS_TOKEN_TIME = ACCESS_TOKEN_TIME;
        this.REFRESH_TOKEN_TIME_WEB = REFRESH_TOKEN_TIME_WEB;
        this.REFRESH_TOKEN_TIME_APP = REFRESH_TOKEN_TIME_APP;
        this.SECRET_KEY = SECRET_KEY;
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(this.SECRET_KEY));
    }

    public TokenInfo generateTokenInfo(String email, int identify) {
        return TokenInfo.builder()
                .email(email)
                .accessToken(createAccessToken(email))
                .refreshToken(createRefreshToken(identify))
                .build();
    }

    private String createAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(
                        System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(ACCESS_TOKEN_TIME)))
                .signWith(key)
                .compact();
    }

    private String createRefreshToken(int identify) {
        long date = (identify == 0) ? TimeUnit.SECONDS.toMillis(REFRESH_TOKEN_TIME_WEB) : TimeUnit.SECONDS.toMillis(REFRESH_TOKEN_TIME_APP);
        return Jwts.builder()
                .setExpiration(new Date(
                        System.currentTimeMillis() + date))
                .signWith(key)
                .compact();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰, 사용자 이메일: {}", e.getClaims().getSubject());
            return e.getClaims();
        } catch (JwtException e) {
            log.error("토큰 파싱 실패: {}", e.getMessage());
            throw new RuntimeException("토큰 파싱 실패: {}", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (MalformedJwtException e) {
            log.info("잘못된 형식의 JWT 토큰입니다.");
        } catch (SignatureException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        } catch (JwtException e) {
            log.info("JWT 토큰 처리 중 알 수 없는 예외가 발생했습니다.");
        }
        return false;
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

}
