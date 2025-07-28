package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

@Data
public class TimeKeepingViolationFilter implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateFilter toDate;
    private LocalDateFilter fromDate;
    private TimeKeepingViolationTypeFilter type;
    private BooleanFilter isActive;
    private UUIDFilter employeeIds;
    private UUIDFilter workspaceIds;
    private StringFilter company;
    private UUIDFilter explanationId;
    public UUIDFilter employeeIds() {
        if (employeeIds == null) {
            employeeIds = new UUIDFilter();
        }
        return employeeIds;
    }

    public UUIDFilter workspaceIds() {
        if (workspaceIds == null) {
            workspaceIds = new UUIDFilter();
        }
        return workspaceIds;
    }

    public TimeKeepingViolationFilter() {
    }

    public TimeKeepingViolationFilter(TimeKeepingViolationFilter filter) {
        this.toDate = filter.optionalToDate().map(LocalDateFilter::copy).orElse(null);
        this.fromDate = filter.optionalFromDate().map(LocalDateFilter::copy).orElse(null);
        this.type = filter.optionalType().map(TimeKeepingViolationTypeFilter::copy).orElse(null);
        this.isActive = filter.isActive == null ? null : filter.isActive.copy();
        this.employeeIds = filter.employeeIds == null ? null : filter.employeeIds.copy();
        this.workspaceIds = filter.workspaceIds == null ? null : filter.workspaceIds.copy();
        this.company = filter.company == null ? null : filter.company.copy();

    }

    @Override
    public TimeKeepingViolationFilter copy() {
        return new TimeKeepingViolationFilter(this);
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

    public Optional<LocalDateFilter> optionalToDate() {
        return Optional.ofNullable(toDate);
    }

    public LocalDateFilter toDate() {
        if (toDate == null) {
            setToDate(new LocalDateFilter());
        }
        return toDate;
    }

    public Optional<LocalDateFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public LocalDateFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new LocalDateFilter());
        }
        return fromDate;
    }

    public Optional<TimeKeepingViolationTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TimeKeepingViolationTypeFilter type() {
        if (type == null) {
            setType(new TimeKeepingViolationTypeFilter());
        }
        return type;
    }

    public static class TimeKeepingViolationTypeFilter extends Filter<TimeKeepingViolationType> {

        public TimeKeepingViolationTypeFilter() {
        }

        public TimeKeepingViolationTypeFilter(TimeKeepingViolationTypeFilter filter) {
            super(filter);
        }

        @Override
        public TimeKeepingViolationTypeFilter copy() {
            return new TimeKeepingViolationTypeFilter(this);
        }
    }
}
