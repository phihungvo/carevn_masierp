package com.carevn.masi.sdk.impl;

import com.carevn.masi.sdk.dto.CallDetail;
import com.carevn.masi.sdk.dto.CallHistoryQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class VoipImpl implements com.carevn.masi.sdk.Voip {
    private String apiKey = "0a9d331af7e3a5185acc73fed591f4fcadf786bc";
    private String apiSecret = "6d08ed94f96261a7d491e3e01914a2ee395fa376";
    private String accessToken;
    private ZonedDateTime accessTokenExpiresAt = ZonedDateTime.now().minusDays(1);
    private final WebClient webClient;

    public VoipImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getAccessToken() {
        if (accessToken != null && accessTokenExpiresAt.isAfter(ZonedDateTime.now())) {
            return Mono.just(accessToken);
        }
        final String ENDPOINT = "http://auth2.voip24h.vn/api/token";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.createObjectNode()
                .put("api_key", apiKey)
                .put("api_secert", apiSecret);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        try {
            String json = objectWriter.writeValueAsString(jsonNode);
            return webClient.post()
                    .uri(ENDPOINT)
                    .header("Content-Type", "application/json")
                    .header("X-VOIP24H-AUTH", "JSON web token (JWT), see Authentication")
                    .bodyValue(json)
                    .retrieve()
                    .bodyToMono(ObjectNode.class)
                    .flatMap(jsonObj -> {
                        String token = jsonObj.get("data").get("response").get("data").get("IsToken").asText("");
                        accessToken = token;
                        accessTokenExpiresAt = ZonedDateTime.now().plusMinutes(30);
                        return Mono.just(token);
                    })
                    .doOnError(e -> System.out.println("Error: " + e.getMessage()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Mono.error(e);
        }

    }

    @Override
    public Mono<Collection<CallDetail>> getCallHistory(com.carevn.masi.sdk.dto.CallHistoryQuery query) {
        var tokenMono = getAccessToken();
        var ENDPOINT = "http://graph.voip24h.vn/call/find";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = query.toJson(objectMapper);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        try {
            String json = objectWriter.writeValueAsString(jsonNode);
            return tokenMono.flatMap(token -> {
                return webClient.post()
                        .uri(ENDPOINT)
                        .header("Content-Type", "application/json")
                        .header("X-VOIP24H-AUTH", token)
                        .header("Authorization", "Bearer " + token)
                        .bodyValue(json)
                        .retrieve()
                        .bodyToMono(ObjectNode.class)
                        .map(jsonObj -> jsonObj.get("data").get("response").get("data"))
                        .flatMap(jsonString -> {
                            ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<CallDetail>>() {
                            });
                            try {
                                Collection<CallDetail> rs = objectReader.readValue(jsonString);
                                return Mono.just(rs);
                            } catch (IOException e) {
                                return Mono.error(e);
                            }
                        });
            });
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }

    }


}
