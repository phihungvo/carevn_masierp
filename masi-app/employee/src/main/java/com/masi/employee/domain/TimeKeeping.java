package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.employee.domain.enumeration.*;
import com.masi.employee.service.dto.HourWorkedSerialize;
import com.masi.employee.service.dto.TimeKeepingDTO;

import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.*;
import java.util.Objects;
import java.util.UUID;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TimeKeeping.
 */
@Table("time_keeping")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeKeeping implements Serializable, Persistable<UUID> {

    private static final float WFH_HOURS = 8f;
    @Serial
    private static final long serialVersionUID = 132333L;

    public static final float MAX_WORK_HOURS = 8f;
    public static final ZonedDateTime DEFAULT_MORNING_START_WORK_TIME = LocalDateTime.of(2021, 1, 1, 1, 30).atZone(ZoneId.of("UTC"));
    public static final ZonedDateTime DEFAULT_MORNING_END_WORK_TIME = LocalDateTime.of(2021, 1, 1, 5, 0).atZone(ZoneId.of("UTC"));
    public static final ZonedDateTime DEFAULT_AFTERNOON_START_WORK_TIME = LocalDateTime.of(2021, 1, 1, 6, 0).atZone(ZoneId.of("UTC"));
    public static final ZonedDateTime DEFAULT_AFTERNOON_END_WORK_TIME = LocalDateTime.of(2021, 1, 1, 10, 30).atZone(ZoneId.of("UTC"));

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("date")
    private LocalDate date;

    @Column("first_check_in")
    private ZonedDateTime firstCheckIn;

    @Column("last_check_in")
    private ZonedDateTime lastCheckIn;

    @Column("hours_worked")
    @JsonSerialize(using = HourWorkedSerialize.class)
    private Float hoursWorked;

    @Column("note")
    private String note;

    @NotNull(message = "must not be null")
    @Column("is_abnormal")
    private Boolean isAbnormal = false;

    @Column("is_day_off")
    private Boolean isDayOff = false;

    @NotNull(message = "must not be null")
    @Column("is_override")
    private Boolean isOverride = false;

    @NotNull(message = "must not be null")
    @Column("locked")
    private Boolean locked = false;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @NotNull(message = "must not be null")
    @Column("last_updated_at")
    private ZonedDateTime lastUpdatedAt = ZonedDateTime.now();

    @Column("leave_type")
    private LeaveType leaveType;

    @Column("leave_day_type")
    private LeaveRequestDayType leaveDayType;

    @Column("violation_type")
    private TimeKeepingViolationType violationType;

    @Column("is_work_from_home")
    @Builder.Default
    private Boolean isWorkFromHome = false;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnore
    private Employee employee;

    @Transient
    @JsonIgnore
    private TimeKeepingViolation violation;

    @Transient
    @JsonIgnoreProperties(value = {"timeKeepings", "employee"}, allowSetters = true)
    @JsonIgnore
    private PersonalMonthlyTimesheet personalMonthlyTimesheet;

    @Column("personal_monthly_timesheet_id")
    private UUID personalMonthlyTimesheetId;

    @Column("employee_id")
    private UUID employeeId;

    @Transient
    @JsonIgnore
    private LeaveDay leaveDay;

    @Transient
    private EmployeeShiftDetail employeeShiftDetail;

    @Getter
    @Column("type")
    @Setter
    private TimeKeepingType timeKeepingType = TimeKeepingType.HOUR;

    @Transient
    @Column("completion_percent")
    private Float completion_percent;

    public void resolveHours(TimeKeepingType timeKeepingType, WorkspaceType workspaceType) {
        // nếu là loại công việc làm việc giờ thì mới tính giờ làm việc, văn phòng, nếu không thì không tính
        if (firstCheckIn == null || lastCheckIn == null || !TimeKeepingType.HOUR.equals(timeKeepingType) || !WorkspaceType.OFFICE.equals(workspaceType)) {
            return;
        }

//        set day of firstCheckIn and lastCheckIn to date 2021-01-01
        firstCheckIn = firstCheckIn.withYear(2021).withMonth(1).withDayOfMonth(1).withSecond(0).withNano(0);
        lastCheckIn = lastCheckIn.withYear(2021).withMonth(1).withDayOfMonth(1).withSecond(0).withNano(0);
        ZonedDateTime startMorning;
        ZonedDateTime endMorning;
        if (firstCheckIn.isBefore(DEFAULT_MORNING_START_WORK_TIME)) {
            startMorning = DEFAULT_MORNING_START_WORK_TIME;
        } else if (firstCheckIn.isBefore(DEFAULT_MORNING_END_WORK_TIME)) {
            startMorning = firstCheckIn;
        } else {
            startMorning = DEFAULT_MORNING_END_WORK_TIME;
        }
        if (lastCheckIn.isAfter(DEFAULT_MORNING_END_WORK_TIME)) {
            endMorning = DEFAULT_MORNING_END_WORK_TIME;
        } else if (lastCheckIn.isBefore(DEFAULT_MORNING_START_WORK_TIME)) {
            endMorning = DEFAULT_MORNING_START_WORK_TIME;
        } else {
            endMorning = lastCheckIn;
        }
        System.out.println("Start morning: " + startMorning);
        System.out.println("End morning: " + endMorning);
        var workTimeOfMorning = Duration.between(startMorning, endMorning).toMinutes() * 1.0f / 60;
        // thứ 7 làm việc buổi sáng thì không cần tính buổi chiều
        if (this.getFirstCheckIn().getDayOfWeek() == DayOfWeek.SATURDAY) {
            this.hoursWorked = workTimeOfMorning;
            return;
        }
        System.out.println("Work time of morning: " + workTimeOfMorning);

        ZonedDateTime startAfternoon;
        ZonedDateTime endAfternoon;

        if (firstCheckIn.isBefore(DEFAULT_AFTERNOON_START_WORK_TIME)) {
            startAfternoon = DEFAULT_AFTERNOON_START_WORK_TIME;
        } else if (firstCheckIn.isBefore(DEFAULT_AFTERNOON_END_WORK_TIME)) {
            startAfternoon = firstCheckIn;
        } else {
            startAfternoon = DEFAULT_AFTERNOON_END_WORK_TIME;
        }

        if (lastCheckIn.isAfter(DEFAULT_AFTERNOON_END_WORK_TIME)) {
            endAfternoon = DEFAULT_AFTERNOON_END_WORK_TIME;
        } else if (lastCheckIn.isBefore(DEFAULT_AFTERNOON_START_WORK_TIME)) {
            endAfternoon = DEFAULT_AFTERNOON_START_WORK_TIME;
        } else {
            endAfternoon = lastCheckIn;
        }
        System.out.println("=======================================");
        System.out.println("Start afternoon: " + startAfternoon);
        System.out.println("End afternoon: " + endAfternoon);
        var workTimeOfAfternoon = Duration.between(startAfternoon, endAfternoon).toMinutes() * 1.0f / 60;
        System.out.println("Work time of afternoon: " + workTimeOfAfternoon);
        var totalWorkTime = workTimeOfMorning + workTimeOfAfternoon;
        this.hoursWorked = (float) (Math.floor(totalWorkTime * 10) / 10);
        System.out.println("---------------------------------------");
        System.out.println("Total work time: " + totalWorkTime);
    }

    public static void main(String[] args) {
        TimeKeeping timeKeeping = new TimeKeeping();
//        "firstCheckIn": "2021-01-01T01:30:23Z",
//            "lastCheckIn": "2021-01-01T23:01:47Z",
        timeKeeping.setFirstCheckIn(ZonedDateTime.of(2021, 1, 1, 1, 30, 50, 0, ZoneId.of("UTC")));
        timeKeeping.setLastCheckIn(ZonedDateTime.of(2021, 1, 1, 23, 1, 47, 0, ZoneId.of("UTC")));
        timeKeeping.resolveHours(TimeKeepingType.HOUR, WorkspaceType.OFFICE);
        System.out.printf("Hours worked: %.1f", (Math.floor(timeKeeping.hoursWorked * 10) / 10));


    }

    @JsonIgnore
    public boolean isPaidLeave() {
        return this.leaveType == LeaveType.ANNUAL_LEAVE ||
                this.leaveType == LeaveType.MATERNITY_LEAVE ||
                this.leaveType == LeaveType.WEDDING_LEAVE ||
                this.leaveType == LeaveType.FUNERAL_LEAVE ||
                this.leaveType == LeaveType.SICK_LEAVE ||
                this.leaveType == LeaveType.COMPENSATION_LEAVE;
    }

    @JsonIgnore
    public boolean isAnnualLeave() {
        if (this.leaveType == null) {
            return false;
        }
        if (TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST.equals(this.violationType)) {
            return false;
        }
        if (Boolean.TRUE.equals(this.isDayOff))
            return false;
        if (LeaveType.COMPENSATION_LEAVE.equals(this.leaveType)) {
            return false;
        }
        // todo: check if this is correct
        return !LeaveType.UNPAID_LEAVE.equals(this.leaveType);
    }

    public boolean isUnpaidLeave() {
        return this.leaveType == LeaveType.UNPAID_LEAVE
                || TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST.equals(this.violationType);
    }

    @JsonIgnore
    public boolean hasValidWorkedHours() {
        if (this.getLeaveDayType() != null) return false;
        if (TimeKeepingType.LOADING_UNLOADING.equals(this.timeKeepingType)) {
            return this.hoursWorked != null && this.hoursWorked > 0;
        }
        if (TimeKeepingType.MIXING_FLOUR.equals(this.timeKeepingType)) {
            return this.hoursWorked != null && this.hoursWorked > 0 && this.hoursWorked <= 80;
        }
        return this.hoursWorked != null && this.hoursWorked > 0 && this.hoursWorked < 24;
    }

    @JsonIgnore
    public boolean hasInsufficientWorkedHours() {
        return this.hoursWorked != null &&
                this.hoursWorked < 8 &&
                this.firstCheckIn != null &&
                !LeaveRequestDayType.HALF_DAY.equals(this.leaveDayType);
    }

    @JsonIgnore
    public boolean isAbsent() {

        return !Boolean.TRUE.equals(this.isDayOff) &&
                this.firstCheckIn == null &&
                (this.hoursWorked == null || this.hoursWorked == 0) &&
                this.leaveType == null &&
                this.leaveDayType == null;
    }

    @JsonIgnore
    public boolean isMissingCheckout() {
        return this.firstCheckIn != null && this.lastCheckIn == null;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public TimeKeeping id(UUID id) {
        this.setId(id);
        return this;
    }


    public TimeKeeping date(LocalDate date) {
        this.setDate(date);
        return this;
    }


    public TimeKeeping firstCheckIn(ZonedDateTime firstCheckIn) {
        this.setFirstCheckIn(firstCheckIn);
        return this;
    }


    public TimeKeeping lastCheckIn(ZonedDateTime lastCheckIn) {
        this.setLastCheckIn(lastCheckIn);
        return this;
    }

    public void calHoursWorked() {
        if (this.firstCheckIn == null || this.lastCheckIn == null) {
            return;
        }
        this.hoursWorked = (float) (Math.floor(Duration.between(this.firstCheckIn, this.lastCheckIn).toMinutes() * 1.0 / 60 * 10) / 10);
    }

    public Float getHoursWorked() {
        if (LeaveRequestDayType.HALF_DAY.equals(this.leaveDayType)) {
            return 4f;
        }
        return this.hoursWorked == null || this.hoursWorked < 0 ? 0 : this.hoursWorked;
    }

    public TimeKeeping hoursWorked(Float hoursWorked) {
        this.setHoursWorked(hoursWorked);
        return this;
    }


    public TimeKeeping note(String note) {
        this.setNote(note);
        return this;
    }


    public TimeKeeping isAbnormal(Boolean isAbnormal) {
        this.setIsAbnormal(isAbnormal);
        return this;
    }


    public TimeKeeping isDayOff(Boolean isDayOff) {
        this.setIsDayOff(isDayOff);
        return this;
    }


    public TimeKeeping isOverride(Boolean isOverride) {
        this.setIsOverride(isOverride);
        return this;
    }


    public TimeKeeping locked(Boolean locked) {
        this.setLocked(locked);
        return this;
    }


    public TimeKeeping createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }


    public TimeKeeping lastUpdatedAt(ZonedDateTime lastUpdatedAt) {
        this.setLastUpdatedAt(lastUpdatedAt);
        return this;
    }


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public TimeKeeping setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = employee != null ? employee.getId() : null;
    }

    public TimeKeeping employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }


    public void setViolation(TimeKeepingViolation timeKeepingViolation) {
        if (this.violation != null) {
            this.violation.setTimeKeeping(null);
        }
        if (timeKeepingViolation != null) {
            timeKeepingViolation.setTimeKeeping(this);
        }
        this.violation = timeKeepingViolation;
    }

    public TimeKeeping violation(TimeKeepingViolation timeKeepingViolation) {
        this.setViolation(timeKeepingViolation);
        return this;
    }


    public void setPersonalMonthlyTimesheet(PersonalMonthlyTimesheet personalMonthlyTimesheet) {
        this.personalMonthlyTimesheet = personalMonthlyTimesheet;
        this.personalMonthlyTimesheetId = personalMonthlyTimesheet != null ? personalMonthlyTimesheet.getId() : null;
    }

    public TimeKeeping personalMonthlyTimesheet(PersonalMonthlyTimesheet personalMonthlyTimesheet) {
        this.setPersonalMonthlyTimesheet(personalMonthlyTimesheet);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeKeeping)) {
            return false;
        }
        return getId() != null && getId().equals(((TimeKeeping) o).getId());
    }


    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }


    // prettier-ignore
    @Override
    public String toString() {
        return "TimeKeeping{" +
                "id=" + getId() +
                ", date='" + getDate() + "'" +
                ", firstCheckIn='" + getFirstCheckIn() + "'" +
                ", lastCheckIn='" + getLastCheckIn() + "'" +
                ", hoursWorked=" + getHoursWorked() +
                ", note='" + getNote() + "'" +
                ", isAbnormal='" + getIsAbnormal() + "'" +
                ", isDayOff='" + getIsDayOff() + "'" +
                ", isOverride='" + getIsOverride() + "'" +
                ", locked='" + getLocked() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", lastUpdatedAt='" + getLastUpdatedAt() + "'" +
                "}";
    }

    private void clearViolation() {
        leaveDayType = null;
        leaveType = null;
        violationType = null;
        violation = null;
    }


    public void applyCharacteristics(String character) {
        clearViolation();
        if (StringUtils.isBlank(character)) {
            return;
        }
        if (this.employeeShiftDetail == null) {
            this.employeeShiftDetail = new EmployeeShiftDetail();
        }

        character = character.toUpperCase().trim();
        switch (character) {
            case "P":
                // nghỉ nguyên ngày
                this.setLeaveDayType(LeaveRequestDayType.FULL_DAY);
                this.leaveType = LeaveType.ANNUAL_LEAVE;

                this.employeeShiftDetail.setIsShiftOff(true);
                this.employeeShiftDetail.setIsPaidShift(false);
                this.employeeShiftDetail.setLeaveDayType(LeaveType.ANNUAL_LEAVE_FULL_DAY);
                this.employeeShiftDetail.setNote("Nghỉ Phép một ngày");
                break;
            case "PO":
                // nghỉ nửa ngày
                this.setLeaveDayType(LeaveRequestDayType.HALF_DAY);
                this.leaveType = LeaveType.ANNUAL_LEAVE;

                this.employeeShiftDetail.setIsShiftOff(true);
                this.employeeShiftDetail.setIsPaidShift(false);
                this.employeeShiftDetail.setLeaveDayType(LeaveType.ANNUAL_LEAVE_HALF_DAY);
                this.employeeShiftDetail.setNote("Nghỉ Phép nữa ngày");
                break;
            case "NB":
                // nghỉ bù
                this.setLeaveType(LeaveType.COMPENSATION_LEAVE);
                this.setLeaveDayType(LeaveRequestDayType.FULL_DAY);

                this.employeeShiftDetail.setIsShiftOff(true);
                this.employeeShiftDetail.setIsPaidShift(true);
                this.employeeShiftDetail.setLeaveDayType(LeaveType.ANNUAL_LEAVE_FULL_DAY);
                this.employeeShiftDetail.setNote("Nghỉ Phép bù ngày");
                break;
            case "V":
                // nghỉ phép
                this.setLeaveType(LeaveType.UNPAID_LEAVE);
                this.setLeaveDayType(LeaveRequestDayType.FULL_DAY);

                this.employeeShiftDetail.setIsShiftOff(true);
                this.employeeShiftDetail.setIsPaidShift(false);
                this.employeeShiftDetail.setLeaveDayType(LeaveType.ANNUAL_LEAVE_FULL_DAY);
                this.employeeShiftDetail.setNote("Nghỉ Phép không có tiền");
                break;
            case "OFF":
                // ngày nghỉ
                this.setIsDayOff(true);

                this.employeeShiftDetail.setIsShiftOff(true);
                this.employeeShiftDetail.setIsPaidShift(true);
                this.employeeShiftDetail.setNote("OFF thì nghỉ");
                break;
            case "KP":
                // nghỉ không phép
                this.setHoursWorked(-2f);
                this.setViolationType(TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST);

                this.employeeShiftDetail.setIsShiftOff(true);
                this.employeeShiftDetail.setIsPaidShift(false);
                this.employeeShiftDetail.setViolationType(TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST);
                this.employeeShiftDetail.setNote("Tự ý nghỉ không phép");
                break;
            case "0":
                this.setHoursWorked(0f);
                break;
            case "NULL":
                this.setHoursWorked(-1f);
                break;
            case "WFH":
                this.employeeShiftDetail.setIsPaidShift(true);
                this.setIsWorkFromHome(true);
                this.setHoursWorked(WFH_HOURS);

                this.employeeShiftDetail.setIsWfh(true);
                this.employeeShiftDetail.setCompletionPercent(WFH_HOURS);
                this.employeeShiftDetail.setNote("Tự ý nghỉ không phép");
                break;
            default:
                break;
        }
    }

    public String resolveTimesheetDayValue() {

        if (Boolean.TRUE.equals(this.getIsDayOff())) {
            return "OFF";
        }
        if (Boolean.TRUE.equals(this.isWorkFromHome)) {
            return "WFH";
        }
        // ngầm qui định -2 là không phép, -1 là không có giờ làm việc để khỏi xung đột
        if (Objects.nonNull(this.hoursWorked) && this.hoursWorked == -2) {
            return "KP";
        }
        if (Objects.nonNull(this.hoursWorked) && this.hoursWorked == -1) {
            return "";
        }
        if (Objects.nonNull(this.getViolationType())
                && TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST.equals(this.getViolationType())) {
            return ""; // đáng lẽ phải là KP (nghỉ không phép) nhưng để trống
        }
        if (this.hasValidWorkedHours()) {
            int intValue = this.hoursWorked.intValue();
            boolean isInt = this.hoursWorked - intValue == 0;
            if (isInt) {
                return String.valueOf(intValue);
            }
            return String.format("%.1f", (Math.floor(this.hoursWorked * 10) / 10));
        }

        if (Objects.nonNull(this.getLeaveDayType())) {
            if (this.isAnnualLeave() &&
                    Objects.nonNull(this.getLeaveType())) {
                if (this.getLeaveDayType().equals(LeaveRequestDayType.FULL_DAY)) {
                    return "P";
                } else {
                    return "PO";
                }
            }
            if (this.getLeaveType().equals(LeaveType.COMPENSATION_LEAVE)) {
                return "NB";
            }
            if (this.getLeaveType().equals(LeaveType.UNPAID_LEAVE)) {
                return "V";
            }
        }
        if (Objects.nonNull(this.hoursWorked) && this.hoursWorked == 0) {
            return "0";
        }

        return "";
    }

    public void setFirstCheckIn(ZonedDateTime firstCheckIn) {
        if (Objects.nonNull(firstCheckIn) && Objects.nonNull(this.lastCheckIn) && firstCheckIn.equals(this.lastCheckIn)) {
            this.lastCheckIn = null;
            this.hoursWorked = null;
        }
        this.firstCheckIn = firstCheckIn;
    }

    public void setLastCheckIn(ZonedDateTime lastCheckIn) {
        if (Objects.nonNull(firstCheckIn) && Objects.nonNull(this.lastCheckIn) && firstCheckIn.equals(this.lastCheckIn)) {
            this.lastCheckIn = null;
            this.hoursWorked = null;
            return;
        }
        this.lastCheckIn = lastCheckIn;
    }

    public void setHoursWorked(Float hoursWorked) {
        if (Objects.nonNull(firstCheckIn) && Objects.nonNull(this.lastCheckIn) && firstCheckIn.equals(this.lastCheckIn)) {
            this.lastCheckIn = null;
            this.hoursWorked = null;
            return;
        }
        this.hoursWorked = hoursWorked;
    }

    public TimeKeepingDTO toDto() {
        TimeKeepingDTO timeKeepingDTO = new TimeKeepingDTO();
        timeKeepingDTO.setId(this.id);
        timeKeepingDTO.setDate(this.date);
        if (Objects.nonNull(this.date)) {
            timeKeepingDTO.setZonedDate(this.date.atStartOfDay(ZoneOffset.UTC).toInstant().atZone(ZoneId.of("UTC")));
        }
        this.resolveTimesheetDayValue();
        timeKeepingDTO.setIsDayOff(this.isDayOff);
        timeKeepingDTO.setFirstCheckIn(this.firstCheckIn);
        timeKeepingDTO.setLastCheckIn(this.lastCheckIn);

        timeKeepingDTO.setHoursWorked(this.hoursWorked);
        if (this.hoursWorked != null && this.hoursWorked >= 0)
            timeKeepingDTO.setTotalCompletionPercent(this.hoursWorked);

        timeKeepingDTO.setNote(this.note);
        timeKeepingDTO.setIsAbnormal(this.isAbnormal);
        timeKeepingDTO.setIsOverride(this.isOverride);
        timeKeepingDTO.setLocked(this.locked);
        timeKeepingDTO.setEmployee(Objects.nonNull(this.employee) ? this.employee.toDto() : null);
        timeKeepingDTO.setCreatedAt(createdAt);
        timeKeepingDTO.setLastUpdatedAt(lastUpdatedAt);
        timeKeepingDTO.setType(this.timeKeepingType);
        timeKeepingDTO.setIsWorkFromHome(this.isWorkFromHome);
        timeKeepingDTO.setEmployeeId(this.employeeId);
        return timeKeepingDTO;
    }

    public void partialUpdate(TimeKeepingDTO dto) {
        this.id = dto.getId();
        this.hoursWorked = dto.getHoursWorked();
        if (Boolean.TRUE.equals(dto.getIsDayOff())) {
            this.hoursWorked = 0f;
            this.setIsDayOff(true);
        } else {
            this.setIsDayOff(false);
        }
    }


}
