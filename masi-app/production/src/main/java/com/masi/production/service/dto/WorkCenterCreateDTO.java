package com.masi.production.service.dto;

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
public class WorkCenterCreateDTO implements Serializable {

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private WorkCenterStatusEnum status;

    @NotNull(message = "must not be null")
    private UUID companyId;

    private ZonedDateTime lastCheckedAt;

}
