package com.masi.employee.service.web.client;

import com.masi.employee.service.dto.ConfigDTO;
import com.masi.employee.service.dto.StandardWorkScheduleConfigDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.reactive.ResponseUtil;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ConfigClient {
    private final WebClient webClient;
    private final String HOST = "masiutility";
    private final String FILE_API = "/api/configs";
    private final String ENDPOINT;

    public ConfigClient(@Qualifier("webClientConsul") WebClient webClient) {

        this.webClient = webClient;
        ENDPOINT = "http://" + HOST + FILE_API;
    }

    private Mono<String> getJwtToken() {

        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(authentication -> {
                if (authentication instanceof JwtAuthenticationToken authenticationToken) {
                    return authenticationToken.getToken().getTokenValue();
                }
                return "";
            }).switchIfEmpty(Mono.just(""));

    }

    public Mono<ConfigDTO> getConfig(String key, Map<String, String> body) {
        var defaultValue = new ConfigDTO();
        defaultValue.setValue(body.get("value"));
        return getJwtToken().flatMap(token -> webClient
            .post()
            .uri(ENDPOINT + "/{key}", key)
            .header("Authorization", "Bearer " + token)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(ConfigDTO.class)
            .doOnError(throwable -> {
                log.error("Error when getConfig", throwable);
            })
            .onErrorReturn(defaultValue)
            .switchIfEmpty(Mono.just(defaultValue))
        );
    }

    public Mono<List<StandardWorkScheduleConfigDTO>> getStandardWorkScheduleConfig(String workspaceType, String company) {
        return getJwtToken().flatMap(token -> webClient
            .get()
            .uri(ENDPOINT + "/standard-work-schedule-configs?company={company}&workspaceType={workspaceType}", company, workspaceType)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToFlux(StandardWorkScheduleConfigDTO.class)
            .doOnError(throwable -> {
                log.error("Error when getStandardWorkScheduleConfig", throwable);
            })
            .collectList()
            .switchIfEmpty(Mono.just(List.of()))
            .onErrorReturn(List.of())
        );
    }

    public Mono<ConfigDTO> getConfig(String key, String defaultValue) {
        Map<String, String> body = Map.of("value", defaultValue);
        return this.getConfig(key, body);
    }

    public Mono<ConfigDTO> getConfig(String key, String defaultValue, String type) {
        Map<String, String> body = Map.of("value", defaultValue, "type", type);
        return this.getConfig(key, body);
    }

    public Mono<ConfigDTO> getConfig(String key, String defaultValue, String type, String description) {
        Map<String, String> body = Map.of("value", defaultValue, "type", type, "description", description);
        return this.getConfig(key, body);
    }

}
