package com.carevn.masi.service.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
public class GroupUserDTO {
    private UUID groupId;
    private Set<UUID> userIds;

    public GroupUserDTO() {
        // Empty constructor needed for Jackson.
    }

    public GroupUserDTO(UUID groupId, Set<UUID> userIds) {
        this.groupId = groupId;
        this.userIds = userIds;
    }

}
