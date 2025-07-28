package com.carevn.masi.dto;

import com.carevn.masi.constants.AuthoritiesConstants;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Data
public class UserJWTDetail {
    private UUID userId = UUID.randomUUID();
    private String companyId = "";
    private String groupId = "";
    private String groupUId = "";
    private String authorities;
    private Collection<String> roles;
    private Collection<String> permissions;

    public UserJWTDetail(UUID userId, String companyId, String groupId, String authorities) {
        this.userId = userId;
        this.companyId = companyId;
        this.groupId = groupId;
        this.authorities = authorities;
    }

    public UserJWTDetail() {
    }

    public void resolveRolesAndPermissions() {
        var authoritiesList = StringUtils.split(authorities, " ");
        roles = Arrays.stream(authoritiesList).filter(a -> a.startsWith("ROLE_")).toList();
        permissions = Arrays.stream(authoritiesList).filter(a -> !a.startsWith("PERMISSION")).toList();
    }

    public boolean isHasAbove(String role) {
        return roles.stream().anyMatch(r -> {
            return AuthoritiesConstants.isHigherPriority(r, role);
        });
    }

    public boolean isHasPermission(String permission) {
        return permissions.stream().anyMatch(p -> {
            return p.equals(permission);
        });
    }



}
