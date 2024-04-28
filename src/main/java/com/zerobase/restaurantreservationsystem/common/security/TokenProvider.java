package com.zerobase.restaurantreservationsystem.common.security;

import com.zerobase.restaurantreservationsystem.common.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    // private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1hour
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7days
    private static final String KEY_ROLE = "role";
    private final MemberService memberService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    public String generateToken(String username, String role) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add(KEY_ROLE, role)
                .build();

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(getSecretKey())
                .compact();
    }

    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    public String getRole(String token) {
        return this.parseClaims(token).get(KEY_ROLE, String.class);
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;

        Claims claims = this.parseClaims(token);
        return claims.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "HmacSHA512");
    }

    public Authentication getAuthentication(String token) {
        // Username과 Role을 합쳐서 인증해야 함
        // e.g., "abc@gmail.com ROLE_CUSTOMER"
        String username = getUsername(token);
        String role = getRole(token);
        UserDetails userDetails = memberService.loadUserByUsername(username + " " + role);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
