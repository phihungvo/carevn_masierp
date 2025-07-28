package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.LeaveRequestStatus;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.UUIDFilter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

@Data
public class LeaveRequestFilter implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LeaveRequestStatusFilter status;
    private LeaveTypeFilter type;
    private BooleanFilter isActive;
    private WorkspaceType workspaceType;
    private UUIDFilter workspaceIds;
    private UUIDFilter employeeIds;
    public LeaveRequestFilter() {
    }

    public LeaveRequestFilter(LeaveRequestFilter filter) {
        this.status = filter.optionalStatus().map(LeaveRequestStatusFilter::copy).orElse(null);
        this.type = filter.optionalType().map(LeaveTypeFilter::copy).orElse(null);
        this.isActive = filter.isActive == null ? null : filter.isActive.copy();
    }

    @Override
    public LeaveRequestFilter copy() {
        return new LeaveRequestFilter(this);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        isActive().setEquals(isActive);
    }

    public Optional<LeaveRequestStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public LeaveRequestStatusFilter status() {
        if (status == null) {
            setStatus(new LeaveRequestStatusFilter());
        }
        return status;
    }


    public Optional<LeaveTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public LeaveTypeFilter type() {
        if (type == null) {
            setType(new LeaveTypeFilter());
        }
        return type;
    }

    public static class LeaveRequestStatusFilter extends Filter<LeaveRequestStatus> {

        public LeaveRequestStatusFilter() {
        }

        public LeaveRequestStatusFilter(LeaveRequestStatusFilter filter) {
            super(filter);
        }

        @Override
        public LeaveRequestStatusFilter copy() {
            return new LeaveRequestStatusFilter(this);
        }
    }

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
}
