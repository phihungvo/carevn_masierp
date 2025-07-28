package com.masi.sale.service.web.client;

import com.masi.sale.service.dto.FileAttachmentDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Collection;
import java.util.UUID;

@Service
public class FileClient {
    private final WebClient webClient;
    private final String HOST = "masiutility";
    private final String FILE_API = "/api/file-attachments";
    private final String ENDPOINT;

    public FileClient(@Qualifier("webClientConsul") WebClient webClient) {

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

    public Flux<FileAttachmentDTO> getFileAttachmentsByListIds(Collection<UUID> ids) {
        return getJwtToken().flatMapMany(token -> webClient
            .get()
            .uri(uriBuilder -> {
                return uriBuilder
                    .host(HOST)
                    .path(FILE_API + "/list")
                    .queryParam("ids", ids)
                    .build();
            }).header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToFlux(FileAttachmentDTO.class)
            .onErrorResume(e -> Flux.empty()));
    }

    public Mono<FileAttachmentDTO> getFileAttachment(UUID id) {
        return getJwtToken().flatMap(token -> webClient
            .get()
            .uri(ENDPOINT + "/{id}/detail", id)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(FileAttachmentDTO.class)
            .onErrorReturn(FileAttachmentDTO.builder().id(id).build()));
    }
}
