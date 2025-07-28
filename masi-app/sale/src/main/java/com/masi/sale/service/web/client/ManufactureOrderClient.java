package com.masi.sale.service.web.client;

import java.time.Instant;
import java.util.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import static com.masi.sale.security.SecurityUtils.JWT_ALGORITHM;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.masi.sale.service.dto.EmployeeDTO;
import com.masi.sale.service.dto.ManufactureOrderDTO;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ManufactureOrderClient {
    private final WebClient webClient;
    private final String HOST = "masiproduction";
    private final String EMPLOYEE_API = "/api/manufacture-orders";
    private final String ENDPOINT;
    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    private JwtEncoder jwtEncoder(String jwtKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey(jwtKey)));
    }

    private SecretKey getSecretKey(String jwtKey) {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    private String genToken() {
        log.info("Generating token");
        JwtEncoder encoder = jwtEncoder(jwtKey);

        var now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(60))
                .subject("system")
                .claims(customClain -> customClain.put("authorities", Collections.singletonList("ROLE_ADMIN")))
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public ManufactureOrderClient(@Qualifier("webClientConsul") WebClient webClient) {

        this.webClient = webClient;
        ENDPOINT = "http://" + HOST + EMPLOYEE_API;
    }

    private Mono<String> getJwtToken() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {
                    if (authentication instanceof JwtAuthenticationToken authenticationToken) {
                        return authenticationToken.getToken().getTokenValue();
                    }
                    return genToken();
                }).switchIfEmpty(Mono.just(genToken()));
    }

    public Mono<List<ManufactureOrderDTO>> getByListIds(java.util.List<UUID> listId) {
        var nonNullList = listId.stream().filter(Objects::nonNull).toList();
        var set = new HashSet<>(nonNullList);
        return getJwtToken().flatMap(token -> webClient
                .get()
                .uri(uriBuilder -> {
                    var a = uriBuilder
                            .host(HOST)
                            .path(EMPLOYEE_API + "/list-by-orders")
                            .queryParam("ids", set)
                            .build();
                    return a;
                })
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(ManufactureOrderDTO.class)
            .collectList()
            .onErrorReturn(new ArrayList<>()));
    }

}
