package com.masi.production.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.WorkCenter;
import com.masi.production.domain.enumeration.WorkCenterStatusEnum;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.WorkCenter} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkCenterDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String name;

    private String code;

    private String note;

    private WorkCenterStatusEnum status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID companyId = UUID.randomUUID();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    private ZonedDateTime lastCheckedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    public void applyUpdate(WorkCenter workCenter) {
        this.setName(workCenter.getName());
        this.setStatus(workCenter.getStatus());
        this.setCompanyId(workCenter.getCompanyId());
        this.setLastCheckedAt(workCenter.getLastCheckedAt());

    }

}
