package com.masi.utility.service.web.client;

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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Collections;

import static com.masi.utility.security.SecurityUtils.JWT_ALGORITHM;

@Service
@Slf4j
public class SaleWebClient {
    private final WebClient webClient;
    private final String HOST = "masisale";
    private final String SALE_API = "/api";
    private final String ENDPOINT;
    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    public SaleWebClient(@Qualifier("webClientConsul") WebClient webClient) {
        this.webClient = webClient;
        ENDPOINT = "http://" + HOST + SALE_API;
    }

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
                .claims(customClaim -> customClaim.put("authorities", Collections.singletonList("ROLE_ADMIN")))
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
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

    public Mono<Void> cleanOldDeletedContract() {
        return getJwtToken()
                .flatMap(token ->
                        webClient.delete()
                                .uri(ENDPOINT + "/contracts/clean-old-deleted-contract")
                                .header("Authorization", "Bearer " + token)
                                .retrieve()
                                .bodyToMono(Void.class)
                                .doOnSuccess(aVoid ->
                                        log.info("Clean old deleted contract success")
                                )
                )
                .onErrorResume(e -> {
                    log.error("Error in cleaning old deleted contract", e);
                    return Mono.empty();
                })
                .then();
    }

    public Mono<Void> sendCustomerBirthdayNotify() {
        return getJwtToken()
                .flatMap(token ->
                        webClient.patch()
                                .uri(ENDPOINT + "/customers/customer-birthday")
                                .header("Authorization", "Bearer " + token)
                                .retrieve()
                                .bodyToMono(Void.class)
                                .doOnSuccess(aVoid -> log.info("sendCustomerBirthdayNotify success"))
                )
                .onErrorResume(e -> {
                    log.error("Error in auto-update-data-time-keeping", e);
                    return Mono.empty();
                })
                .then();
    }

}
