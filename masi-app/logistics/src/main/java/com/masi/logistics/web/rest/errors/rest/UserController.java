package com.masi.logistics.web.rest.errors.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    @GetMapping("/api/user-authorities")
    public Mono<String> getUserAuthorities() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    if (authentication != null) {
                        StringBuilder authorities = new StringBuilder("User Authorities: ");
                        for (GrantedAuthority authority : authentication.getAuthorities()) {
                            authorities.append(authority.getAuthority()).append(" ");
                        }
                        return authorities.toString();
                    } else {
                        return "No authentication available.";
                    }
                });
    }
}
