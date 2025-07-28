package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.WorkPlace;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.service.dto.WorkspaceDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Workspace.
 */
@Table("workspace")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class Workspace implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotNull(message = "must not be null")
    private String name;

    @Column("description")
    private String description;

    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Column("workspace_type")
    private WorkspaceType workspaceType;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = {"workspace", "personalMonthlyTimesheets"}, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    @Column("can_delete")
    private Boolean canDelete = true;

    @Column("normalized_name")
    private String normalizedName;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public void setId(UUID id) {
        this.id = id;
    }

    public Workspace id(UUID id) {
        this.setId(id);
        return this;
    }


    public Workspace name(String name) {
        this.setName(name);
        return this;
    }


    public Workspace description(String description) {
        this.setDescription(description);
        return this;
    }


    public Workspace isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }


    public Workspace createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }


    public Workspace lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setWorkspace(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setWorkspace(this));
        }
        this.employees = employees;
    }


    @Override
    public UUID getId() {
        return this.id;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Workspace setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Workspace)) {
            return false;
        }
        return getName() != null && getName().equals(((Workspace) o).getName());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Workspace{" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }

    @JsonIgnore
    public Workspace partialUpdate(WorkspaceDTO dto) {
        if (dto.getName() != null) {
            this.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            this.setDescription(dto.getDescription());
        }
        if (dto.getWorkspaceType() != null) {
            this.setWorkspaceType(dto.getWorkspaceType());
        }

        return this;
    }

    public WorkspaceDTO toDTO() {
        WorkspaceDTO dto = new WorkspaceDTO();
        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setDescription(this.getDescription());
        dto.setIsActive(this.getIsActive());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setLastUpdated(this.getLastUpdated());
        dto.setCanDelete(this.getCanDelete());
        dto.setNormalizedName(this.getNormalizedName());
        return dto;
    }

    public WorkPlace changeWorkspaceType() {
        WorkspaceType newWorkspaceType = this.getWorkspaceType();
        switch (newWorkspaceType) {
            case OFFICE:
                return WorkPlace.OFFICE;
            case FACTORY:
                return WorkPlace.FACTORY;
            default:
                throw new IllegalArgumentException("Unknown workspace type: " + newWorkspaceType);
        }
    }
}
