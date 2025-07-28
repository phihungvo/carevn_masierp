package com.masi.production.service.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masi.production.service.dto.*;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import lombok.Data;
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

import static com.masi.production.security.SecurityUtils.JWT_ALGORITHM;


@Service
@Slf4j
public class LogisticClient {
    private final WebClient webClient;
    private final String HOST = "masilogistics";
    private final String UOM_API = "/api/uoms";
    private final String WAREHOUSE_API = "/api/warehouses";
    private final String INVENTORY_API = "/api/inventory-transactions";
    private final String INVENTORIES_API = "/api/inventories";
    private final String INVENTORIES_STORAGE_API = "/api/inventories-storages";
    private final String ITEM_API = "/api/items";

    private final String ENDPOINT_UOM;
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
                    return Mono.empty();
                });
        });
    }


    public Flux<UomDTO> getUomByListIds(List<UUID> ids) {
        var nonNullList = ids.stream().filter(Objects::nonNull).distinct().toList();
        if (nonNullList.isEmpty()) {
            return Flux.empty();
        }
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
            .doOnNext(uomDTO -> {
                log.info("Received UomDTO: {}", uomDTO);
            })
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

    public Flux<MaterialDTO> getMaterialByListIds(List<UUID> ids) {
        var nonNullList = ids.stream().filter(Objects::nonNull).distinct().toList();
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
            .bodyToFlux(MaterialDTO.class)
            .onErrorResume(e -> {
                e.printStackTrace();
                return Mono.error(e);
            }));
    }


    public Mono<Void> updateInventoriesStorage(UUID id, UUID itemId, Float code) {
        if (id == null || itemId == null) {
            log.error("ID or itemId is null. Skipping update.");
            return Mono.empty();
        }

        return getJwtToken()
                .flatMap(token -> {
                    log.info("Token retrieved: {}", token);
                    return webClient
                            .patch()
                            .uri(uriBuilder -> uriBuilder
                                    .host(HOST)
                                    .path(INVENTORIES_STORAGE_API + "/protein/{id}/{itemId}")
                                    .build(id, itemId))
                            .header("Authorization", "Bearer " + token)
                            .bodyValue(code) // Truyền dữ liệu vào body
                            .retrieve()
                            .toBodilessEntity()
                            .then();
                })
                .onErrorResume(e -> {
                    log.error("Error during updateInventoriesStorage for ID: {}, itemId: {}. Error: {}", id, itemId, e.getMessage());
                    return Mono.empty();
                });
    }



    public Mono<Void> createRequestWarehouseRelease(List<ReleaseWarehouseDTO> releaseWarehouses) {
        return getJwtToken().flatMap(token -> {
            System.out.println("Token: " + token);
            return webClient
                .post()
                .uri(uriBuilder -> {
                    var uri = uriBuilder
                        .host(HOST)
                        .path(INVENTORY_API)
                        .build();
                    log.info("URI: {}", uri.toString());
                    return uri;
                })
                .header("Authorization", "Bearer " + token)
                .bodyValue(releaseWarehouses) // Set body của POST request
                .retrieve()
                .bodyToMono(Void.class) // Xử lý response không cần body (nếu không có)
                .onErrorResume(e -> {
                    log.error("Error during createRequestWarehouseRelease: {}", e.getMessage());
                    return Mono.empty();
                });
        });
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
                                item.setPercentProtein(itemNode.get("percentProtein").asInt());
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

    public Mono<WarehouseResponse> getWarehousesByListIds(List<UUID> ids) {
    var nonNullList = ids.stream().filter(Objects::nonNull).distinct().toList();
    return getJwtToken().flatMap(token -> webClient
            .get()
            .uri(uriBuilder -> {
                var uri = uriBuilder
                    .host(HOST)
                    .path(WAREHOUSE_API)
                    .queryParam("id.in", nonNullList)
                    .build();
                log.info("URI getWarehousesByListIds: {}", uri.toString());
                return uri;
            })
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(WarehouseResponse.class)
            .onErrorResume(e -> {
                e.printStackTrace();
                return Mono.empty();
            }))
        .switchIfEmpty(Mono.empty());
    }

    public Mono<InventoriesDTO> createInventoriesByRoutingWarehouse(InventoriesDTO inventoriesDTO) {
        return getJwtToken().flatMap(token -> {
            log.info("Request body: {}", inventoriesDTO);
            return webClient
                .post()
                .uri(uriBuilder -> {
                    var uri = uriBuilder
                        .host(HOST)
                        .path(INVENTORIES_API)
                        .build();
                    log.info("URI createInventoriesByRoutingWarehouse: {}", uri.toString());
                    return uri;
                })
                .header("Authorization", "Bearer " + token)
                .body(Mono.just(inventoriesDTO), InventoriesDTO.class)
                .retrieve()
                .bodyToMono(InventoriesDTO.class)
                .onErrorResume(e -> {
                    log.error("Error during createInventoriesByRoutingWarehouse: {}", e.getMessage());
                    return Mono.empty();
                });
        });
    }

    public Mono<InventoriesDTO> updateInventories(InventoriesDTO inventoriesDTO){
        return getJwtToken().flatMap(token -> {
            log.info("Request body: {}", inventoriesDTO);
            return webClient
                .patch()
                .uri(uriBuilder -> {
                    var uri = uriBuilder
                        .host(HOST)
                        .path(INVENTORIES_API)
                        .queryParam("id", inventoriesDTO.getId())
                        .build();
                    log.info("URI updateInventories: {}", uri.toString());
                    return uri;
                })
                .header("Authorization", "Bearer " + token)
                .body(Mono.just(inventoriesDTO), InventoriesDTO.class)
                .retrieve()
                .bodyToMono(InventoriesDTO.class)
                .onErrorResume(e -> {
                    log.error("Error during updateInventories: {}", e.getMessage());
                    return Mono.empty();
                });
        });
    }

    public Mono<Void> updateVolumeInventoriesStorage(List<MaterialManuFactureDTO> materialManuFactureDTOS) {
        return getJwtToken().flatMap(token -> {
            log.info("Token retrieved: {}", token);
            return webClient
                    .patch()
                    .uri(uriBuilder -> {
                        var uri = uriBuilder
                                .host(HOST)
                                .path(INVENTORIES_STORAGE_API + "/update-volume")
                                .build();
                        log.info("URI updateVolumeInventoriesStorage: {}", uri.toString());
                        return uri;
                    })
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .bodyValue(materialManuFactureDTOS)
                    .retrieve()
                    .toBodilessEntity()
                    .then()
                    .onErrorResume(e -> {
                        log.error("Error during updateVolumeInventoriesStorage: {}", e.getMessage());
                        return Mono.empty();
                    });
        });
    }

    public Mono<Void> createInventoriesStorage(InventoriesRequest inventoriesRequest) {
        return getJwtToken().flatMap(token -> {
            log.info("Request body: {}", inventoriesRequest);
            return webClient
                    .post()
                    .uri(uriBuilder -> {
                        var uri = uriBuilder
                                .host(HOST)
                                .path(INVENTORIES_API)
                                .build();
                        log.info("URI createInventoriesStorage: {}", uri.toString());
                        return uri;
                    })
                    .header("Authorization", "Bearer " + token)
                    .bodyValue(inventoriesRequest) // Set body of POST request
                    .retrieve()
                    .toBodilessEntity() // Handle response without body
                    .then()
                    .onErrorResume(e -> {
                        log.error("Error during createInventoriesStorage: {}", e.getMessage());
                        return Mono.empty();
                    });
        });
    }



    @Data
    public static class WarehouseResponse{
        private List<WarehouseDTO> data;

        public WarehouseResponse() {
        }

        public List<WarehouseDTO> getData() {
            return data;
        }
        public void setData(List<WarehouseDTO> data) {
            this.data = data;
        }
    }
}
