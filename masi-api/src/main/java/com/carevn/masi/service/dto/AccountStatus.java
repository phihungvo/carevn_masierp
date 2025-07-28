package com.carevn.masi.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AccountStatus {
    public enum Status {
        ACTIVE, INACTIVE, NOT_FOUND
    }

    private UUID id;
    private Status status;
}
