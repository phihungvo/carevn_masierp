package com.masi.logistics.service.dto;

import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.ItemType} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemTypeDTO implements Serializable {

    private UUID id;

    private String code;

    private String name;

    private String description;

    private Boolean isActive;

    private Boolean isDeleted;

    private ZonedDateTime createdAt;

    private String createdBy;

    private ZonedDateTime updatedAt;

    private String updatedBy;

    private ZonedDateTime deletedAt;

    private String deletedBy;

    private String company;

    private String department;

    private com.masi.logistics.domain.enumeration.ItemType itemType;
}
