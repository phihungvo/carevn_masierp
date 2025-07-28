package com.carevn.masi.service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GroupAuthorityDTO {

    @NotNull
    private UUID groupId;

    @NotNull
    @NotEmpty
    private Set<String> authorities;

    public GroupAuthorityDTO() {
        // Empty constructor needed for Jackson.
    }

    public GroupAuthorityDTO(UUID groupId, Set<String> authorities) {
        this.groupId = groupId;
        this.authorities = authorities;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}
