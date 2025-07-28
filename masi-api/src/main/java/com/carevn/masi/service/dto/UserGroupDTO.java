package com.carevn.masi.service.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserGroupDTO {
    private UUID userId;
    private Set<UUID> groupIds;
}
