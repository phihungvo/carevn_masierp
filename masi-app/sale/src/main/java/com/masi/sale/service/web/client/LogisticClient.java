package com.masi.sale.service.web.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masi.sale.service.dto.ItemDTO;
import com.masi.sale.service.dto.UomDTO;
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

import static com.masi.sale.security.SecurityUtils.JWT_ALGORITHM;

@Service
@Slf4j
public class LogisticClient {
    private final WebClient webClient;
    private final String HOST = "masilogistics";
    private final String UOM_API = "/api/uoms";
    private final String ITEM_API = "/api/items";
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
                    return Mono.error(e);
                }))
                .switchIfEmpty(Flux.empty());

    }

    public Flux<ItemDTO> getItemByListIds(List<UUID> ids) {
        var nonNullList = ids.stream().filter(Objects::nonNull).distinct().toList();
        return getJwtToken().flatMapMany(token -> webClient
                        .get()
                        .uri(uriBuilder -> {
                            var uri = uriBuilder
                                    .host(HOST)
                                    .path(ITEM_API)
                                    .queryParam("id.in", nonNullList)
                                    .build();
                            log.info("URI: {}", uri.toString());
                            return uri;
                        })
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(String.class)
                        .flatMapMany(rawResponse -> {
                            try {
                                ObjectMapper objectMapper = new ObjectMapper();
                                JsonNode jsonNode = objectMapper.readTree(rawResponse);
                                JsonNode dataNode = jsonNode.get("data");
                                List<ItemDTO> items = new ArrayList<>();
                                if (dataNode.isArray()) {
                                    for (JsonNode itemNode : dataNode) {
                                        ItemDTO item = new ItemDTO();
                                        item.setId(UUID.fromString(itemNode.get("id").asText()));
                                        item.setCode(itemNode.get("code").asText());
                                        item.setName(itemNode.get("name").asText());
                                        item.setUomId(UUID.fromString(itemNode.get("uomId").asText()));
                                        item.setPercentProtein((itemNode.get("percentProtein") == null ? 0f : itemNode.get("percentProtein").floatValue()));
                                        items.add(item);
                                    }
                                }
                                return Flux.fromIterable(items);
                            } catch (JsonProcessingException e) {
                                log.error("Error processing JSON response", e);
                                return Flux.empty();
                            }
                        })
                        .onErrorResume(e -> {
                            e.printStackTrace();
                            return Flux.empty();
                        }))
                .switchIfEmpty(Flux.empty());
    }

}
