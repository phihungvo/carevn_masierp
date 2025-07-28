package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.WorkspaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

import java.io.Serializable;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.LeaveType;

/**
 * A DTO for the {@link com.masi.employee.domain.LeaveRegimeRequest} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRegimeRequestFilterDTO implements Serializable, Criteria {

    public static class LeaveTypeFilter extends Filter<LeaveType> {

        public LeaveTypeFilter() {
        }

        public LeaveTypeFilter(LeaveTypeFilter filter) {
            super(filter);
        }

        @Override
        public LeaveTypeFilter copy() {
            return new LeaveTypeFilter(this);
        }
    }

    public static class LeaveRegimeRequestStatusFilter extends Filter<LeaveRegimeRequestStatus> {

        public LeaveRegimeRequestStatusFilter() {
        }

        public LeaveRegimeRequestStatusFilter(LeaveRegimeRequestStatusFilter filter) {
            super(filter);
        }

        @Override
        public LeaveRegimeRequestStatusFilter copy() {
            return new LeaveRegimeRequestStatusFilter(this);
        }
    }

    private LeaveTypeFilter leaveType;
    private LeaveRegimeRequestStatusFilter status;
    private ZonedDateTimeFilter startLeaveDate;
    private ZonedDateTimeFilter endLeaveDate;
    private ZonedDateTimeFilter fromTime;
    private ZonedDateTimeFilter toTime;
    private BooleanFilter isDeleted;
    private StringFilter companyId;
    private UUIDFilter employeeId;
    private StringFilter department;
//    private WorkspaceType workspaceType;

    public LeaveRegimeRequestFilterDTO(LeaveRegimeRequestFilterDTO leaveRegimeRequestFilterDTO) {
        if (leaveRegimeRequestFilterDTO != null) {
            this.isDeleted = leaveRegimeRequestFilterDTO.isDeleted == null ? null
                    : leaveRegimeRequestFilterDTO.isDeleted.copy();
            this.leaveType = leaveRegimeRequestFilterDTO.leaveType == null ? null
                    : leaveRegimeRequestFilterDTO.leaveType.copy();
            this.status = leaveRegimeRequestFilterDTO.status == null ? null : leaveRegimeRequestFilterDTO.status.copy();
            this.startLeaveDate = leaveRegimeRequestFilterDTO.startLeaveDate == null ? null
                    : leaveRegimeRequestFilterDTO.startLeaveDate.copy();
            this.companyId = leaveRegimeRequestFilterDTO.companyId == null ? null
                    : leaveRegimeRequestFilterDTO.companyId.copy();
            this.employeeId = leaveRegimeRequestFilterDTO.employeeId == null ? null
                    : leaveRegimeRequestFilterDTO.employeeId.copy();
            this.endLeaveDate = leaveRegimeRequestFilterDTO.endLeaveDate == null ? null
                    : leaveRegimeRequestFilterDTO.endLeaveDate.copy();
            this.department = leaveRegimeRequestFilterDTO.department == null ? null
                    : leaveRegimeRequestFilterDTO.department.copy();
            this.fromTime = leaveRegimeRequestFilterDTO.fromTime == null ? null
                    : leaveRegimeRequestFilterDTO.fromTime.copy();
            this.toTime = leaveRegimeRequestFilterDTO.toTime == null ? null
                    : leaveRegimeRequestFilterDTO.toTime.copy();
        }
    }

    public LeaveRegimeRequestFilterDTO copy() {
        return new LeaveRegimeRequestFilterDTO(this);
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            isDeleted = new BooleanFilter();
        }
        return isDeleted;
    }

    public LeaveTypeFilter leaveType() {
        if (leaveType == null) {
            leaveType = new LeaveTypeFilter();
        }
        return leaveType;
    }

    public LeaveRegimeRequestStatusFilter status() {
        if (status == null) {
            status = new LeaveRegimeRequestStatusFilter();
        }
        return status;
    }

    public ZonedDateTimeFilter startLeaveDate() {
        if (startLeaveDate == null) {
            startLeaveDate = new ZonedDateTimeFilter();
        }
        return startLeaveDate;
    }

    public ZonedDateTimeFilter endLeaveDate() {
        if (endLeaveDate == null) {
            endLeaveDate = new ZonedDateTimeFilter();
        }
        return endLeaveDate;
    }

    public StringFilter companyId() {
        if (companyId == null) {
            companyId = new StringFilter();
        }
        return companyId;
    }

    public StringFilter department() {
        if (department == null) {
            department = new StringFilter();
        }
        return department;
    }

    public UUIDFilter employeeId() {
        if (employeeId == null) {
            employeeId = new UUIDFilter();
        }
        return employeeId;
    }

    public ZonedDateTimeFilter fromTime() {
        if (fromTime == null) {
            fromTime = new ZonedDateTimeFilter();
        }
        return fromTime;
    }

    public ZonedDateTimeFilter toTime() {
        if (toTime == null) {
            toTime = new ZonedDateTimeFilter();
        }
        return toTime;
    }
}
