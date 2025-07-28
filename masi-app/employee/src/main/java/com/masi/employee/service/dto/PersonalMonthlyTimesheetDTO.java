package com.masi.employee.service.dto;

import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.TimesheetReviewStatus;
import com.masi.employee.domain.enumeration.WorkspaceType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.PersonalMonthlyTimesheet} entity.
 */
@Data
public class PersonalMonthlyTimesheetDTO implements Serializable {

    private static final long serialVersionUID = -9092187837140311617L;

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private LocalDate month;

    @NotNull(message = "must not be null")
    private TimesheetReviewStatus status;

    @NotNull(message = "must not be null")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    private ZonedDateTime lastUpdated;

    private MonthlyTimeSheetReviewDTO review;

     private EmployeeDTO employee;

    private List<TimeKeeping> timeKeepings;

    private TimeKeepingType timeKeepingType;

    // Giờ ca	Lễ 300%	Ngày off hưởng nguyên lương	Phép năm	Tổng giờ tại NM	Tổng công tại NM	Tổng công	Ngày off trong tháng
    //giờ ca
    private float shiftHours;
    //lễ 300%
    private float holiday300;
    //ngày off hưởng nguyên lương
    private float offDay;
    //phép năm
    private float annualLeave;
    //tổng giờ tại NM
    private float totalHoursAtFactory;
    //tổng công tại NM
    private float totalWorkAtFactory;
    //tổng công
    private float totalWork;
    //ngày off trong tháng
    private float offDayInMonth;
    // số ngày wfh trong tháng
    private float totalWorkFromHome = 0f;
    private WorkspaceType workspaceType;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalMonthlyTimesheetDTO)) {
            return false;
        }

        PersonalMonthlyTimesheetDTO personalMonthlyTimesheetDTO = (PersonalMonthlyTimesheetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personalMonthlyTimesheetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalMonthlyTimesheetDTO{" +
            "id='" + getId() + "'" +
            ", month='" + getMonth() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", employee=" + getEmployee() +
            ", annual=" + getAnnualLeave() +
            ", review=" + getReview() +
            "}";
    }

}
