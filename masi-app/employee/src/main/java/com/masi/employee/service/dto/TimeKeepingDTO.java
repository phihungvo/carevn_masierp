package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.masi.employee.web.rest.TimeKeepingRecordResource.ENTITY_NAME;

/**
 * A DTO for the {@link com.masi.employee.domain.TimeKeeping} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeKeepingDTO implements Serializable {

    private UUID id;

    private LocalDate date;

    private ZonedDateTime zonedDate;

    private ZonedDateTime firstCheckIn;

    private ZonedDateTime lastCheckIn;

    @JsonSerialize(using = HourWorkedSerialize.class)
    private Float hoursWorked;

    private String note;

    private Boolean isDayOff;

    private Boolean isAbnormal;

    private Boolean isOverride;

    private Boolean locked;

    private EmployeeDTO employee;

    private UUID employeeId;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdatedAt;

    private PersonalMonthlyTimesheetDTO personalMonthlyTimesheet;

    private UUID personalMonthlyTimesheetId;

    private String character;

    private TimeKeepingType type = TimeKeepingType.HOUR;

    private Boolean isWorkFromHome = false;

    private Float totalCompletionPercent;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeKeepingDTO timeKeepingDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timeKeepingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeKeepingDTO{" +
            "id='" + getId() + "'" +
            ", date='" + getDate() + "'" +
            ", zonedDate='" + getZonedDate() + "'" +
            ", firstCheckIn='" + getFirstCheckIn() + "'" +
            ", lastCheckIn='" + getLastCheckIn() + "'" +
            ", hoursWorked=" + getHoursWorked() +
            ", note='" + getNote() + "'" +
            ", isAbnormal='" + getIsAbnormal() + "'" +
            ", isDayOff='" + getIsDayOff() + "'" +
            ", isOverride='" + getIsOverride() + "'" +
            ", locked='" + getLocked() + "'" +
            ", employee=" + getEmployee() +
            ", employeeId='" + getEmployeeId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdatedAt='" + getLastUpdatedAt() + "'" +
            ", personalMonthlyTimesheet=" + getPersonalMonthlyTimesheet() +
            "}";
    }

    public TimeKeeping toEntity() {
        TimeKeeping timeKeeping = new TimeKeeping();
        timeKeeping.setId(this.id);
        timeKeeping.setDate(this.date);
        timeKeeping.setFirstCheckIn(this.firstCheckIn);
        timeKeeping.setLastCheckIn(this.lastCheckIn);
        if(this.firstCheckIn != null && this.lastCheckIn != null && this.firstCheckIn.equals(this.lastCheckIn)) {
             this.lastCheckIn = null;
        }
        timeKeeping.setHoursWorked(this.hoursWorked);
        timeKeeping.setNote(this.note);
        timeKeeping.setIsAbnormal(this.isAbnormal);
        timeKeeping.setIsOverride(this.isOverride);
        timeKeeping.setLocked(this.locked);
        timeKeeping.setCreatedAt(this.createdAt);
        timeKeeping.setLastUpdatedAt(this.lastUpdatedAt);
        timeKeeping.setEmployeeId(this.employeeId);
        timeKeeping.setPersonalMonthlyTimesheetId(this.personalMonthlyTimesheetId);
        timeKeeping.setTimeKeepingType(this.type);
        timeKeeping.setIsWorkFromHome(this.isWorkFromHome);
        return timeKeeping;
    }


    public boolean isValid() {
        if (this.hoursWorked != null && this.hoursWorked > 0 && Boolean.TRUE.equals(this.isDayOff))
            throw new BadRequestAlertException("Invalid hours worked && isDayOff", ENTITY_NAME, "hoursWorkedInvalid");
        return true;
    }
}
