package com.masi.employee.service;

import com.masi.employee.service.dto.*;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
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

import static com.masi.employee.security.SecurityUtils.JWT_ALGORITHM;


@Service
@Slf4j
public class LogisticClient {
    private final WebClient webClient;
    private final String HOST = "masilogistics";
    private final String UOM_API = "/api/uoms";
    private final String WAREHOUSE_API = "/api/warehouses";

    private final String ENDPOINT_UOM;
    private final String ENDPOINT_WAREHOUSE;
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

    public LogisticClient(@Qualifier("webClientConsul") WebClient webClient) {

        this.webClient = webClient;
        ENDPOINT_WAREHOUSE = "http://" + HOST + WAREHOUSE_API;
        ENDPOINT_UOM = "http://" + HOST + UOM_API;
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

    public Mono<UomDTO> getUomById(UUID id) {
        return getJwtToken().flatMap(token -> {
            System.out.println("Token: " + token);
            return webClient
                    .get()
                    .uri(ENDPOINT_UOM + "/{id}", id)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(UomDTO.class)
                    .onErrorResume(e -> {
                        e.printStackTrace();
                        return Mono.error(e);
                    });
        });
    }


    public Flux<UomDTO> getUomByListIds(List<UUID> ids) {
        var nonNullList = ids.stream().filter(Objects::nonNull).toList();
        return getJwtToken().flatMapMany(token -> webClient
                .get()
                .uri(uriBuilder -> {
                    var a = uriBuilder
                            .host(HOST)
                            .path(UOM_API + "/list")
                            .queryParam("ids", nonNullList)
                            .build();
                    log.info("URI: {}", a.toString());
                    return a;
                })
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(UomDTO.class)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.empty();
                }));
    }

    public Mono<WarehouseDTO> getWarehouseById(UUID id) {
        return getJwtToken().flatMap(token -> {
            System.out.println("Token: " + token);
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .host(HOST)
                            .path(WAREHOUSE_API + "/{id}")
                            .build(id))
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(WarehouseDTO.class)
                    .onErrorResume(e -> {
                        e.printStackTrace();
                        return Mono.empty();
                    });
        });
    }

    public Mono<UniformOrderService.UniformOrderResponse> getUniformOrderResponse(UniformOrderDTO uniformOrderDTO){
        return getJwtToken().flatMap(token -> {
            return webClient.post().uri(ENDPOINT_UOM + "/uniform-order-mapping").header("Authorization", "Bearer " + token)
                    .bodyValue(uniformOrderDTO)
                    .retrieve()
                    .bodyToMono(UniformOrderService.UniformOrderResponse.class)
                    .onErrorResume(e -> {
                        e.printStackTrace();
                        return Mono.empty();
            });
        });
    }

    public Mono<UniformOrderService.UniformOrderResponse> getUniformReleaseResponse(UniformReleaseDTO uniformRelease){
        return getJwtToken().flatMap(token -> {
            return webClient.post().uri(ENDPOINT_UOM + "/uniform-release-mapping").header("Authorization", "Bearer " + token)
                .bodyValue(uniformRelease)
                .retrieve()
                .bodyToMono(UniformOrderService.UniformOrderResponse.class)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.empty();
                });
        });
    }

    public Flux<SuppliersDTO> getSuppliers(int page, int size) {
        return getJwtToken().flatMapMany(token -> webClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .host(HOST)
                .path("/api/suppliers")
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(DataResponse.class)
            .flatMapMany(response -> Flux.fromIterable(response.getData()))
            .onErrorResume(e -> {
                e.printStackTrace();
                return Flux.empty();
            }));
    }

    public static class DataResponse {
        private List<SuppliersDTO> data;

        // Getter và Setter
        public List<SuppliersDTO> getData() {
            return data;
        }

        public void setData(List<SuppliersDTO> data) {
            this.data = data;
        }
    }
}
