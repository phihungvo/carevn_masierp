package com.masi.logistics.service.web.client;

import com.carevn.masi.dto.EmployeeDTO;
import com.masi.logistics.service.dto.ContractSaleDTO;
import com.masi.logistics.service.dto.OrderSaleDTO;
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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.*;

import static com.carevn.masi.utils.SecurityUtils.JWT_ALGORITHM;


@Service
@Slf4j
public class SaleClient {
    private final WebClient webClient;
    private final String HOST = "masisale";
    private final String ORDER_API = "/api/orders";
    private final String CONTRACT_API = "/api/contract";

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

    public SaleClient(@Qualifier("webClientConsul") WebClient webClient) {

        this.webClient = webClient;
        ENDPOINT = "http://" + HOST + ORDER_API;
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

    public Flux<ContractSaleDTO> getContractByListIds(List<UUID> ids) {
        var nonNullList = ids.stream().filter(Objects::nonNull).distinct().toList();
        if (nonNullList.isEmpty()) {
            return Flux.empty();
        }
        return getJwtToken().flatMapMany(token -> webClient
                .post()
                .uri(uriBuilder -> {
                    var a = uriBuilder
                            .host(HOST)
                            .path(CONTRACT_API + "/list")
                            .build();
                    log.info("URI: {}", a.toString());
                    return a;
                })
                .header("Authorization", "Bearer " + token)
                .bodyValue(nonNullList)
                .retrieve()
                .bodyToFlux(ContractSaleDTO.class)
                .onErrorResume(e -> {
                    return Flux.empty();
                }));
    }


    public Flux<OrderSaleDTO> getOrderByListIds(List<UUID> ids) {
        var nonNullList = ids.stream().filter(Objects::nonNull).distinct().toList();
        if (nonNullList.isEmpty()) {
            return Flux.empty();
        }
        return getJwtToken().flatMapMany(token -> webClient
                .post()
                .uri(uriBuilder -> {
                    var a = uriBuilder
                            .host(HOST)
                            .path(ORDER_API + "/list")
                            .build();
                    log.info("URI: {}", a.toString());
                    return a;
                })
                .header("Authorization", "Bearer " + token)
                .bodyValue(nonNullList)
                .retrieve()
                .bodyToFlux(OrderSaleDTO.class)
                .onErrorResume(e -> {
                    return Flux.empty();
                }));
    }

}
