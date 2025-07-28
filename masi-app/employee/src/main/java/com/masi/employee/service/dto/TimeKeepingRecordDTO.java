package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.TimeKeepingRecord;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.TimeKeepingRecord} entity.
 */
@Schema(description = "Time keeping record entity.")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class TimeKeepingRecordDTO implements Serializable {

    private UUID id;

    @JsonProperty("check-in")
    private ZonedDateTime checkIn;

    @JsonIgnore
    private EmployeeDTO employee;

    @JsonProperty("employee")
    private UUID employeeId;

    private TimeKeepingType timeKeepingType =TimeKeepingType.HOUR;

    private Boolean isCheckOut = false;



    public TimeKeepingRecordDTO() {}

    public TimeKeepingRecordDTO(UUID id, ZonedDateTime checkIn, UUID employeeId) {
        this.id = id;
        this.checkIn = checkIn;
        this.employeeId = employeeId;
    }

    public TimeKeepingRecord toEntity() {
        TimeKeepingRecord timeKeepingRecord = new TimeKeepingRecord();
        timeKeepingRecord.setId(this.id);
        timeKeepingRecord.setCheckIn(this.checkIn);
        timeKeepingRecord.setEmployeeId(this.employeeId);
        return timeKeepingRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeKeepingRecordDTO timeKeepingRecordDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timeKeepingRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeKeepingRecordDTO{" +
            "id='" + getId() + "'" +
            ", checkIn='" + getCheckIn() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
