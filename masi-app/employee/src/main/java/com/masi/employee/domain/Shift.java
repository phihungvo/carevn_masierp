package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Shift.
 */
@Data
@Table("shift")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Shift implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("id_standard_work_schedule_config")
    private UUID idStandardWorkScheduleConfig;

    @Column("shift_name")
    private String shiftName;

    @Column("duration_hours")
    private Float durationHours;

    @Column("hour_start_time")
    private Integer hourStartTime;

    @Column("minute_start_time")
    private Integer minuteStartTime;

    @Column("second_start_time")
    private Integer secondStartTime;

    @Column("hour_end_time")
    private Integer hourEndTime;

    @Column("minute_end_time")
    private Integer minuteEndTime;

    @Column("second_end_time")
    private Integer secondEndTime;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_by")
    private String createdBy;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Shift id(UUID id) {
        this.setId(id);
        return this;
    }

    public Shift idStandardWorkScheduleConfig(UUID idStandardWorkScheduleConfig) {
        this.setIdStandardWorkScheduleConfig(idStandardWorkScheduleConfig);
        return this;
    }

    public Shift shiftName(String shiftName) {
        this.setShiftName(shiftName);
        return this;
    }

    public Shift durationHours(Float durationHours) {
        this.setDurationHours(durationHours);
        return this;
    }

    public Shift hourStartTime(Integer hourStartTime) {
        this.setHourStartTime(hourStartTime);
        return this;
    }

    public Shift minuteStartTime(Integer minuteStartTime) {
        this.setMinuteStartTime(minuteStartTime);
        return this;
    }

    public Shift secondStartTime(Integer secondStartTime) {
        this.setSecondStartTime(secondStartTime);
        return this;
    }

    public Shift hourEndTime(Integer hourEndTime) {
        this.setHourEndTime(hourEndTime);
        return this;
    }

    public Shift minuteEndTime(Integer minuteEndTime) {
        this.setMinuteEndTime(minuteEndTime);
        return this;
    }

    public Shift secondEndTime(Integer secondEndTime) {
        this.setSecondEndTime(secondEndTime);
        return this;
    }

    public Shift company(String company) {
        this.setCompany(company);
        return this;
    }

    public Shift department(String department) {
        this.setDepartment(department);
        return this;
    }

    public Shift isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public Shift createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Shift createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Shift updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Shift updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Shift deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public Shift deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Shift setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
