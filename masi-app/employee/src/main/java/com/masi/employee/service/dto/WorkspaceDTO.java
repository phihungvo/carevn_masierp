package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.masi.employee.domain.enumeration.WorkspaceType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.Workspace} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkspaceDTO implements Serializable {

    private UUID id;

    private String name;

    @JsonIgnore
    private String company;

    private String description;

    private Boolean isActive;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private WorkspaceType workspaceType = WorkspaceType.OFFICE;

    private Boolean canDelete = true;
    private String normalizedName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkspaceDTO that = (WorkspaceDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(isActive, that.isActive) && Objects.equals(createdAt, that.createdAt) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkspaceDTO{" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }

}
