package com.masi.production.service.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masi.production.service.dto.InventoriesDTO;
import com.masi.production.service.dto.ItemDTO;
import com.masi.production.service.dto.OrderDTO;
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
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.masi.production.security.SecurityUtils.JWT_ALGORITHM;

@Service
@Slf4j
public class SaleClient {
    private final WebClient webClient;
    private final String HOST = "masisale";
    private final String ORDER_API = "/api/orders";
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

    public Mono<Void> updateContractMaterial(UUID idManufacture, Collection<UUID> idMaterial, UUID idOrder) {
        return getJwtToken().flatMap(token -> {
            System.out.println("Token: " + token);
            if(idMaterial == null)
                return Mono.empty();

            return webClient
                    .patch()
                    .uri(uriBuilder -> uriBuilder
                            .path(ENDPOINT + "update-contract-material/{idOrder}/{idManufacture}/")
                            .build(idOrder, idManufacture)) // Updated URI to include idOrder
                    .header("Authorization", "Bearer " + token)
                    .bodyValue(idMaterial)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnError(e -> {
                        System.err.println("Error updating contract material: " + e.getMessage());
                    })
                    .onErrorResume(e -> {
                        System.out.println("Handling error and continuing execution.");
                        return Mono.empty();
                    });
        });
    }


    public Flux<OrderDTO> findAllOrderById(List<UUID> uuids) {
        return getJwtToken()
                .flatMapMany(token ->
                        webClient.post()
                                .uri(uriBuilder -> {
                                    var uri = uriBuilder
                                            .host(HOST)
                                            .path(ORDER_API + "/list-id")
                                            .build();
                                    log.info("URI: {}", uri.toString());
                                    return uri;
                                })
                                .header("Authorization", "Bearer " + token)
                                .body(Mono.just(uuids), List.class)
                                .retrieve()
                                .bodyToMono(String.class)
                                .flatMapMany(rawResponse -> {
                                    try {
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        JsonNode jsonNode = objectMapper.readTree(rawResponse);
                                        List<OrderDTO> orders = new ArrayList<>();
                                        if (jsonNode.isArray()) {
                                            for (JsonNode itemNode : jsonNode) {
                                                OrderDTO order = new OrderDTO();
                                                order.setId(UUID.fromString(itemNode.get("id").asText()));
                                                order.setCode(itemNode.get("orderCode").asText());
                                                // Additional fields mapping
                                                orders.add(order);
                                            }
                                        }
                                        return Flux.fromIterable(orders);
                                    } catch (JsonProcessingException e) {
                                        log.error("Error processing JSON response", e);
                                        return Flux.empty();
                                    }
                                })
                                .onErrorResume(e -> {
                                    return Flux.empty();
                                })
                )
                .doOnError(e -> log.error("Error finding orders by ids: {}", e.getMessage())) // Xử lý lỗi
                .onErrorResume(e -> { // Nếu lỗi xảy ra, trả về kết quả trống
                    log.warn("Error occurred, returning empty result.");
                    return Flux.empty();
                });
    }




}
