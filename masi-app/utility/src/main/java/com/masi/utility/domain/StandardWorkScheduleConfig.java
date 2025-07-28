package com.masi.utility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A StandardWorkScheduleConfig.
 */
@Data
@Table("standard_work_schedule_config")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardWorkScheduleConfig implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("day_of_week")
    private Integer dayOfWeek;

    @Column("number_of_shifts")
    private Integer numberOfShifts;

    @Column("work_hours")
    private Integer workHours;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @Column("department_type")
    private String departmentType;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public StandardWorkScheduleConfig id(UUID id) {
        this.setId(id);
        return this;
    }

    public StandardWorkScheduleConfig name(String name) {
        this.setName(name);
        return this;
    }

    public StandardWorkScheduleConfig dayOfWeek(Integer dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
        return this;
    }

    public StandardWorkScheduleConfig numberOfShifts(Integer numberOfShifts) {
        this.setNumberOfShifts(numberOfShifts);
        return this;
    }

    public StandardWorkScheduleConfig workHours(Integer workHours) {
        this.setWorkHours(workHours);
        return this;
    }

    public StandardWorkScheduleConfig department(String department) {
        this.setDepartment(department);
        return this;
    }

    public StandardWorkScheduleConfig company(String company) {
        this.setCompany(company);
        return this;
    }

    public StandardWorkScheduleConfig departmentType(String departmentType) {
        this.setDepartmentType(departmentType);
        return this;
    }

    public StandardWorkScheduleConfig updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public StandardWorkScheduleConfig updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public StandardWorkScheduleConfig setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
