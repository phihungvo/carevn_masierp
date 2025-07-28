package com.masi.production.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.MachineCheckRecord} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MachineCheckRecordDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private Boolean status;

    private String statusNote;

    @NotNull(message = "must not be null")
    private Boolean isActive;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private MachineGroupDTO machineGroup;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getStatusNote() {
        return statusNote;
    }

    public void setStatusNote(String statusNote) {
        this.statusNote = statusNote;
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

    public MachineGroupDTO getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(MachineGroupDTO machineGroup) {
        this.machineGroup = machineGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MachineCheckRecordDTO)) {
            return false;
        }

        MachineCheckRecordDTO machineCheckRecordDTO = (MachineCheckRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, machineCheckRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MachineCheckRecordDTO{" +
            "id='" + getId() + "'" +
            ", status='" + getStatus() + "'" +
            ", statusNote='" + getStatusNote() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", machineGroup=" + getMachineGroup() +
            "}";
    }
}
