package com.masi.employee.service.web.client;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.employee.domain.enumeration.PositionEmployee;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import io.r2dbc.postgresql.codec.Json;
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
public class AuthClient {
    private final WebClient webClient;
    private final String HOST = "masierp";
    private final String EMPLOYEE_API = "/api";
    private final String ENDPOINT;
    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;
    // NAME DEPARTMENT DEFAULT
    private final String HCNS = "HR";
    private final String SALE = "SALE";

    private JwtEncoder jwtEncoder(String jwtKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey(jwtKey)));
    }

    private SecretKey getSecretKey(String jwtKey) {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    private String genToken() {
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

    public AuthClient(@Qualifier("webClientConsul") WebClient webClient) {
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
                    return "";
                }).switchIfEmpty(Mono.just(genToken()));

    }

    public Mono<Map> getEmployeePosition(String request, PositionEmployee positionEmployee, String Company) {
        if (positionEmployee == null) {
            return Mono.empty();
        }
        return switch (positionEmployee) {
            case DEPARTMENT_MANAGER -> this.getEmployeeManagerDepartment(request);
            case PERSONNEL_MANAGER -> this.getEmployeeManagerDepartment(HCNS);
            case ACCOUNTING_MANAGER -> this.getEmployeeManagerDepartment(SALE);
            case DIRECTOR -> this.getEmployeeDirectorCompany(Company);
            default -> Mono.empty();
        };
    }


    public Mono<Map> getEmployeeDirectorCompany(String company) {
//        System.out.println("\n\n\n\n\n\n\n\n\n" + ENDPOINT + "/companies/{company}/director");
        return getJwtToken().flatMap(token -> webClient
                .get()
                .uri(ENDPOINT + "/companies/{company}/director", company)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorResume(e -> {
                    System.out.println(e.getMessage());
                    return Mono.empty();
                }));
    }

    public Mono<Map> getEmployeeManagerDepartment(String department) {
        return getJwtToken().flatMap(token -> webClient
                .get()
                .uri(ENDPOINT + "/groups/{department}/manager", department)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorResume(e -> {
                    System.out.println(e.getMessage());
                    return Mono.empty();
                }));

    }

    public Mono<List<Map>> checkHasAccount(List<UUID> ids) {
        var url = ENDPOINT + "/account/check";
        return getJwtToken().flatMap(token -> webClient
                .post()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .bodyValue(ids)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .onErrorReturn(Collections.emptyList()));
    }

    public Mono<Void> updateOrCreateRoleUserId(UUID employeeId, String role) {
        if (employeeId == null || role == null || role.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Employee ID and role must not be null or empty"));
        }

        var url = ENDPOINT + "/account/update-role/" + employeeId + "/" + role;
        return getJwtToken()
                .flatMap(token -> webClient
                        .post()
                        .uri(url)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .onErrorResume(e -> {
                            System.out.println(e.getMessage());
                            return Mono.empty();
                        }));
    }

    public record UserDTO(UUID id, String userName,
                          @JsonSerialize(using = PgJsonObjectSerializer.class)
                          @JsonDeserialize(using = PgJsonObjectDeserializer.class)
                          Json companyJson,
                          Boolean isActivated){}

    public Flux<UserDTO> getAllUserByListId(List<UUID> ids) {
        var setIds = Set.copyOf(ids);
        var url = ENDPOINT + "/users/list";
        return getJwtToken().flatMapMany(token -> webClient
                .post()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .bodyValue(Map.of("ids", setIds))
                .retrieve()
                .bodyToFlux(UserDTO.class)
                .collectList()
                .flatMapMany(Flux::fromIterable)
            .onErrorResume(e -> {
                System.out.println(e.getMessage());
                return Flux.empty();
            })
            .doOnError(e -> System.out.println(e.getMessage())));
    }
}
