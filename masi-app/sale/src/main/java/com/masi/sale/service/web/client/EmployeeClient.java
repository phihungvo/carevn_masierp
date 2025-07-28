package com.masi.sale.service.web.client;

import com.masi.sale.service.dto.EmployeeDTO;


import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static com.masi.sale.security.SecurityUtils.JWT_ALGORITHM;

@Service
@Slf4j
public class EmployeeClient {
    private final WebClient webClient;
    private final String HOST = "masiemployee";
    private final String EMPLOYEE_API = "/api/employees";
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

    public EmployeeClient(@Qualifier("webClientConsul") WebClient webClient) {

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
            })
            .onErrorReturn(genToken())
            .switchIfEmpty(Mono.just(genToken()));
    }

    public Flux<EmployeeDTO> getEmployees() {
        return getJwtToken().flatMapMany(token -> webClient
            .get()
            .uri(ENDPOINT + "/all")
            .retrieve()
            .bodyToFlux(EmployeeDTO.class)
            .onErrorResume(e -> {
                log.error("Error while fetching employees", e);
                return Flux.empty();
            }));
    }

    public Mono<EmployeeDTO> getEmployee(UUID id) {
        return getJwtToken().flatMap(token -> {
            System.out.println("Token: " + token);
            return webClient
                .get()
                .uri(ENDPOINT + "/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(EmployeeDTO.class)
                .onErrorResume(e -> Mono.empty());
        });
    }

    public Flux<EmployeeDTO> getEmployeesByListIds(List<UUID> ids) {
        var nonNullList = ids.stream().filter(Objects::nonNull).distinct().toList();
        if (nonNullList.isEmpty()) {
            return Flux.empty();
        }
        return getJwtToken().flatMapMany(token -> webClient
                .post()
                .uri(uriBuilder -> {
                    var a = uriBuilder
                            .host(HOST)
                            .path(EMPLOYEE_API + "/list")
                            .build();
                    log.info("URI: {}", a.toString());
                    return a;
                })
                .header("Authorization", "Bearer " + token)
                .bodyValue(nonNullList)
                .retrieve()
                .bodyToFlux(EmployeeDTO.class)
                .onErrorResume(e -> {
                    return Flux.empty();
                }));
    }

}
