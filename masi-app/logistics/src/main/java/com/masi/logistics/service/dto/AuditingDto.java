package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;

@Data
public abstract class AuditingDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    protected String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    protected ZonedDateTime updatedAt = ZonedDateTime.now();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    protected String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    protected ZonedDateTime deletedAt = null;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    protected String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    protected String department;
}
