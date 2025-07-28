package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.service.dto.TimeKeepingRecordDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Time keeping record entity.
 */
@Table("time_keeping_record")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeKeepingRecord implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("check_in")
    private ZonedDateTime checkIn;

    @Transient
    private boolean isPersisted;

    @Transient
    private Employee employee;

    @Column("employee_id")
    private UUID employeeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public TimeKeepingRecord id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getCheckIn() {
        return this.checkIn;
    }

    public TimeKeepingRecord checkIn(ZonedDateTime checkIn) {
        this.setCheckIn(checkIn);
        return this;
    }

    public void setCheckIn(ZonedDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public TimeKeepingRecordDTO toDto() {
        TimeKeepingRecordDTO dto = new TimeKeepingRecordDTO();
        dto.setId(this.id);
        dto.setCheckIn(this.checkIn);
        return dto;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public TimeKeepingRecord setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = employee != null ? employee.getId() : null;
    }

    public TimeKeepingRecord employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public UUID getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(UUID employee) {
        this.employeeId = employee;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeKeepingRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((TimeKeepingRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeKeepingRecord{" +
            "id=" + getId() +
            ", checkIn='" + getCheckIn() + "'" +
            "}";
    }
}
