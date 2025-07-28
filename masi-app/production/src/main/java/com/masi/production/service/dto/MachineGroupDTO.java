package com.masi.production.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.MachineGroup} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MachineGroupDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private Boolean isActive;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private MachineOperationChecklistDTO machineOperationChecklist;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public MachineOperationChecklistDTO getMachineOperationChecklist() {
        return machineOperationChecklist;
    }

    public void setMachineOperationChecklist(MachineOperationChecklistDTO machineOperationChecklist) {
        this.machineOperationChecklist = machineOperationChecklist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MachineGroupDTO)) {
            return false;
        }

        MachineGroupDTO machineGroupDTO = (MachineGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, machineGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MachineGroupDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", machineOperationChecklist=" + getMachineOperationChecklist() +
            "}";
    }
}
