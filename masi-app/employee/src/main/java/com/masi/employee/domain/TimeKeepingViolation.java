package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import com.masi.employee.service.dto.TimeKeepingViolationDTO;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TimeKeepingViolation.
 */
@Data
@Table("time_keeping_violation")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeKeepingViolation implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("type")
    private TimeKeepingViolationType type;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    @Column("last_updated_at")
    private ZonedDateTime lastUpdatedAt;

    @Transient
    private boolean isPersisted;

    @Transient
    @JoinColumn(name = "time_keeping_id", referencedColumnName = "id")
    private TimeKeeping timeKeeping;

    @Transient
    private Employee employee;

    @Transient
    @JsonIgnoreProperties(value = {"employee", "violations"}, allowSetters = true)
    private TimeKeepingExplanation explanation;

    @Column("time_keeping_id")
    private UUID timeKeepingId;

    @Column("employee_id")
    private UUID employeeId;

    @Column("explanation_id")
    private UUID explanationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public TimeKeepingViolation id(UUID id) {
        this.setId(id);
        return this;
    }

    public TimeKeepingViolation type(TimeKeepingViolationType type) {
        this.setType(type);
        return this;
    }

    public TimeKeepingViolation isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public TimeKeepingViolation createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public TimeKeepingViolation lastUpdatedAt(ZonedDateTime lastUpdatedAt) {
        this.setLastUpdatedAt(lastUpdatedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public TimeKeepingViolation setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public TimeKeepingViolation timeKeeping(TimeKeeping timeKeeping) {
        this.setTimeKeeping(timeKeeping);
        return this;
    }

    public TimeKeepingViolation employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public TimeKeepingViolation explanation(TimeKeepingExplanation timeKeepingExplanation) {
        this.setExplanation(timeKeepingExplanation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public TimeKeepingViolationDTO toDto() {
        TimeKeepingViolationDTO dto = new TimeKeepingViolationDTO();
        dto.setId(this.id);
        dto.setType(this.type);
        dto.setIsActive(this.isActive);
        dto.setTimeKeepingId(this.timeKeepingId);
        dto.setEmployeeId(this.employeeId);
        if (Objects.nonNull(this.timeKeeping)) dto.setTimeKeeping(this.timeKeeping.toDto());
        if (Objects.nonNull(this.employee)) dto.setEmployee(this.employee.toDto());
        dto.setLastUpdatedAt(this.lastUpdatedAt);
        dto.setCreatedAt(this.createdAt);
        dto.setExplanationId(this.explanationId);
        return dto;
    }
}
