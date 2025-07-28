package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.service.dto.DayOffDTO;
import jakarta.validation.constraints.*;

import java.time.Period;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DayOff.
 */
@Data
@Table("day_off")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DayOff implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("number_days_off")
    private Float numberDaysOff;

    @Column("use_days_off")
    private Float useDaysOff;

    @Column("now_days_off")
    private Float nowDaysOff;

    @NotNull(message = "must not be null")
    @Column("year")
    private Integer year;

    @NotNull(message = "must not be null")
    @Column("employee_id")
    private UUID employeeId;

    @NotNull(message = "must not be null")
    @Column("annual_leave")
    private UUID annualLeave;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("is_active")
    private Boolean isActive;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public DayOff id(UUID id) {
        this.setId(id);
        return this;
    }

    public DayOff numberDaysOff(Float numberDaysOff) {
        this.setNumberDaysOff(numberDaysOff);
        return this;
    }

    public DayOff year(Integer year) {
        this.setYear(year);
        return this;
    }

    public DayOff employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public DayOff annualLeave(UUID annualLeave) {
        this.setAnnualLeave(annualLeave);
        return this;
    }

    public DayOff lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public DayOff createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public DayOff isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public DayOff setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public DayOffDTO toDTO() {
        DayOffDTO dto = new DayOffDTO();
        dto.setId(this.id);

        LocalDate startDate = LocalDate.of(this.year, (int) (this.nowDaysOff + 0f), 1);
        Period period = Period.between(startDate, LocalDate.now());
        float totalMonths = period.getYears() * 12 + period.getMonths();

        float dayCanUse = this.numberDaysOff / 12 * totalMonths;

        dayCanUse = (float) (Math.floor(dayCanUse * 2) / 2);
        dayCanUse = dayCanUse - this.useDaysOff;
        if (dayCanUse < 0) {
            dayCanUse = 0;
        }
        dto.setNumberDaysOff(dayCanUse);
        dto.setUseDaysOff(this.useDaysOff);

        dto.setYear(this.year);
        dto.setEmployeeId(this.employeeId);
        dto.setAnnualLeave(this.annualLeave);
        dto.setLastUpdated(this.lastUpdated);
        dto.setCreatedDate(this.createdDate);
        dto.setIsActive(this.isActive);
        dto.setNowDaysOff(this.getNowDaysOff());


        return dto;
    }


    public DayOffDTO toDTONotCal() {
        DayOffDTO dto = new DayOffDTO();
        dto.setId(this.id);
        dto.setNumberDaysOff(this.numberDaysOff);
        dto.setUseDaysOff(this.useDaysOff);
        dto.setYear(this.year);
        dto.setEmployeeId(this.employeeId);
        dto.setAnnualLeave(this.annualLeave);
        dto.setLastUpdated(this.lastUpdated);
        dto.setCreatedDate(this.createdDate);
        dto.setIsActive(this.isActive);
        dto.setNowDaysOff(this.getNowDaysOff());
        return dto;
    }
}
