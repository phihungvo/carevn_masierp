package com.carevn.masi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;

import static com.carevn.masi.utils.SecurityUtils.AUTHORITIES_KEY;
import static com.carevn.masi.utils.SecurityUtils.JWT_ALGORITHM;

@Service
public class TokenService {
    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String secret;
    private Instant expiration;
    private String token;
    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    private void createToken() {
        expiration = ZonedDateTime.now().plusDays(1).toInstant();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(Instant.now())
                .expiresAt(expiration)
                .subject("system")
                .claim(AUTHORITIES_KEY, "ROLE_SYSTEM")
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        this.token = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String getToken() {
        if (token == null || Instant.now().isAfter(expiration)) {
            createToken();
        }
        return token;
    }

}
