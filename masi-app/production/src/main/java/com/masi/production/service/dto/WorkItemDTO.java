package com.masi.production.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import com.masi.production.domain.WorkItem;
import lombok.*;

/**
 * A DTO for the {@link com.masi.production.domain.WorkItem} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkItemDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private Boolean isActive;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private Collection<ReceiveMaterialChecklistDTO> receiveMaterialChecklist;

    private Collection<AdditiveMaterialChecklistDTO> additiveMaterialChecklist;

    private Collection<MachineOperationChecklistDTO> machineOperationChecklist;

    private Collection<SteamingProcessChecklistDTO> steamingProcessChecklist;

    private Collection<MetalDetectionChecklistDTO> metalDetectionChecklist;

    private Collection<MixingReportChecklistDTO> mixingReportChecklist;

    private Integer checkListsCount;



    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Collection<BaseCheckListDto> checkLists;


    public WorkItemDTO() {}

    public WorkItemDTO(UUID id, Boolean isActive, ZonedDateTime createdAt) {
        this.id = id;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.lastUpdated = createdAt;
    }


    public WorkItem toEntity() {
        WorkItem workItem = new WorkItem();
        workItem.setId(this.id);
        workItem.setIsActive(this.isActive);
        workItem.setCreatedAt(this.createdAt);
        workItem.setLastUpdated(this.lastUpdated);
        return workItem;
    }

}
