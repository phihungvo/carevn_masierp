package com.carevn.masi.web.rest;

import com.carevn.masi.service.GroupService;
import com.carevn.masi.service.UserService;
import com.carevn.masi.service.web.client.EmployeeClient;
import com.carevn.masi.service.web.client.EmployeeDTO;
import com.carevn.masi.web.rest.vm.LoginVM;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;

import java.io.File;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.carevn.masi.security.SecurityUtils.*;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class AuthenticateController {

    private final Logger log = LoggerFactory.getLogger(AuthenticateController.class);

    private final JwtEncoder jwtEncoder;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds:0}")
    private long tokenValidityInSeconds;

    private final UserService userService;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me:0}")
    private long tokenValidityInSecondsForRememberMe;

    private final ReactiveAuthenticationManager authenticationManager;

    private final GroupService groupService;
    private final EmployeeClient employeeClient;

    public AuthenticateController(JwtEncoder jwtEncoder, UserService userService,
                                  ReactiveAuthenticationManager authenticationManager,
                                  GroupService groupService, EmployeeClient employeeClient) {
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.groupService = groupService;
        this.employeeClient = employeeClient;
    }

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<JWTToken>> authorize(@Valid @RequestBody Mono<LoginVM> loginVM) {
        Map<String, Object> otherClaims = new HashMap<>();
        // Map.of(GROUP_CLAIM_KEY, "group", COMPANY_CLAIM_KEY, "workspace");
        return loginVM
            .flatMap(login -> authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()))
                .flatMap(auth -> employeeClient.getEmployee(UUID.fromString(auth.getName())).switchIfEmpty(Mono.just(new EmployeeDTO()))
                    .zipWith(userService.getById(UUID.fromString(auth.getName())))
                    .map(groupClaim -> {
                        if (Objects.nonNull(groupClaim.getT1().getWorkspace())) {
                            otherClaims.put(GROUP_CLAIM_KEY, String.valueOf(groupClaim.getT1().getWorkspace().getNormalizedName()));
                            otherClaims.put(GROUP_ID_CLAIM_KEY, groupClaim.getT1().getWorkspace().getId());
                        }
                        otherClaims.put(COMPANY_CLAIM_KEY, groupClaim.getT2().getCompanyId());
                        return auth;
                    }))
                .flatMap(auth -> Mono.fromCallable(() -> {
                    String jwt = this.createToken(auth, login.isRememberMe(), otherClaims);
                    Set<String> authorities = auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(x -> x.startsWith("ROLE_"))
                        .collect(Collectors.toSet());
                    return new JWTToken(jwt, authorities);
                })))
            .map(jwtToken -> {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setBearerAuth(jwtToken.getIdToken());
                return new ResponseEntity<>(jwtToken, httpHeaders, HttpStatus.OK);
            });
    }

    /**
     * {@code GET /authenticate} : check if the user is authenticated, and return
     * its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public Mono<String> isAuthenticated(ServerWebExchange request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getPrincipal().map(Principal::getName);
    }

    public String createToken(Authentication authentication, boolean rememberMe, Map<String, Object> otherClaims) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(x -> x.startsWith("ROLE_"))
            .collect(Collectors.joining(" "));
        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(this.tokenValidityInSecondsForRememberMe, ChronoUnit.SECONDS);
        } else {
            validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);
        }

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .claims(stringObjectMap-> stringObjectMap.putAll(otherClaims))
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    public static class JWTToken {

        private String idToken;

        private Set<String> authorities;

        JWTToken(String idToken, Set<String> authorities) {
            this.idToken = idToken;
            this.authorities = authorities;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("authorities")
        Set<String> getAuthorities() {
            return authorities;
        }

        void setAuthorities(Set<String> authorities) {
            this.authorities = authorities;
        }
    }
}
