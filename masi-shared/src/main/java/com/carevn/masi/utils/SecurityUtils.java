package com.carevn.masi.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;

import com.carevn.masi.constants.AuthoritiesConstants;
import com.carevn.masi.dto.UserJWTDetail;

import reactor.core.publisher.Mono;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public static final String AUTHORITIES_KEY = "auth";
    public static final String GROUP_CLAIM_KEY = "group";
    public static final String GROUP_ID_CLAIM_KEY = "group_id";
    public static final String COMPANY_CLAIM_KEY = "company";
    public static final String USER_ID_CLAIM_KEY = "sub";

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Mono<String> getCurrentUserLogin() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.justOrEmpty(extractPrincipal(authentication)));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public static Mono<String> getCompanyId() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .mapNotNull(principal -> {
                    if (principal instanceof Jwt jwt) {
                        String value = jwt.getClaim(COMPANY_CLAIM_KEY);
                        return value;
                    }
                    return "";
                });
    }

    public static Mono<String> getGroupId() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .mapNotNull(principal -> {
                    if (principal instanceof Jwt jwt) {
                        String group = jwt.getClaim(GROUP_CLAIM_KEY);
                        return group;
                    }
                    return "";
                });
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Mono<String> getCurrentUserJWT() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static Mono<Boolean> isAuthenticated() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .map(authorities -> authorities.stream().map(GrantedAuthority::getAuthority)
                        .noneMatch(AuthoritiesConstants.ANONYMOUS::equals));
    }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
    public static Mono<Boolean> hasCurrentUserAnyOfAuthorities(String... authorities) {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .map(authorityList -> authorityList
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(authority -> Arrays.asList(authorities).contains(authority)));
    }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false
     * otherwise.
     */
    public static Mono<Boolean> hasCurrentUserNoneOfAuthorities(String... authorities) {
        return hasCurrentUserAnyOfAuthorities(authorities).map(result -> !result);
    }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static Mono<Boolean> hasCurrentUserThisAuthority(String authority) {
        return hasCurrentUserAnyOfAuthorities(authority);
    }

    public static Mono<UserJWTDetail> getUserJWTDetail() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(principal -> {
                    var userJWTDetail = new UserJWTDetail();
                    if (principal instanceof Jwt jwt) {
                        try {
                            String subStr = (String) jwt.getClaim(USER_ID_CLAIM_KEY);
                            var sub = UUID.fromString(String.valueOf(subStr));
                            String companyIdStr = (String) jwt.getClaim(COMPANY_CLAIM_KEY);
                            if (Objects.nonNull(companyIdStr)) {
                                userJWTDetail.setCompanyId(companyIdStr);
                            }
                            String groupIdStr = (String) jwt.getClaim(GROUP_CLAIM_KEY);
                            if (Objects.nonNull(groupIdStr)) {
                                userJWTDetail.setGroupId(groupIdStr);
                            }
                            String groupUidStr = (String) jwt.getClaim(GROUP_ID_CLAIM_KEY);
                            if (Objects.nonNull(groupUidStr)) {
                                userJWTDetail.setGroupUId(groupUidStr);
                            }
                            userJWTDetail.setUserId(sub);
                            userJWTDetail.setAuthorities((String) jwt.getClaim(AUTHORITIES_KEY));
                            userJWTDetail.resolveRolesAndPermissions();
                            return userJWTDetail;
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            return userJWTDetail;
                        }
                    }
                    return userJWTDetail;
                });
    }
}
