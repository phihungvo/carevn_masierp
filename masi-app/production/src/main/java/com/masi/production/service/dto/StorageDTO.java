package com.masi.production.service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.Storage} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StorageDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    private String code;

    @NotNull(message = "must not be null")
    private String name;

    private String address;

}
