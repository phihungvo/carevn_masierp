package com.masi.production.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class BaseCheckListDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected String type;

    @NotNull(message = "must not be null")
    @NotNull(message = "must not be null")
    protected UUID workItemId;

    public Boolean getActive() {
        return isActive;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = WorkItemDTO.class)
    protected WorkItemDTO workItem;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Boolean isActive = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected ZonedDateTime createdAt = ZonedDateTime.now();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected ZonedDateTime lastUpdated = ZonedDateTime.now();


}
