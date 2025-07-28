package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.LeaveDay} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveDayDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private UUID leaveRequestId;

    @NotNull(message = "must not be null")
    private UUID employeeId;

    @NotNull(message = "must not be null")
    private LocalDate date;

    @NotNull(message = "must not be null")
    private LeaveRequestDayType leaveRequestDayType;

    @NotNull(message = "must not be null")
    private LeaveType leaveType;

    @NotNull(message = "must not be null")
    private Boolean isLocked;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveDayDTO)) {
            return false;
        }

        LeaveDayDTO leaveDayDTO = (LeaveDayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveDayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveDayDTO{" +
            "id='" + getId() + "'" +
            ", leaveRequestId='" + getLeaveRequestId() + "'" +
            ", employeeId='" + getEmployeeId() + "'" +
            ", date='" + getDate() + "'" +
            ", leaveRequestDayType='" + getLeaveRequestDayType() + "'" +
            ", leaveType='" + getLeaveType() + "'" +
            ", isLocked='" + getIsLocked() + "'" +
            "}";
    }
}
