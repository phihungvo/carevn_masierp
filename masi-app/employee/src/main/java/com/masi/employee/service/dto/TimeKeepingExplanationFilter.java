package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.ExplanationStatus;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

public class TimeKeepingExplanationFilter implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private TimeKeepingExplanationStatusFilter status;
    private TimeKeepingExplanationTypeFilter type;
    private BooleanFilter isActive;

    public TimeKeepingExplanationFilter() {
    }

    public TimeKeepingExplanationFilter(TimeKeepingExplanationFilter filter) {
        this.type = filter.optionalType().map(TimeKeepingExplanationTypeFilter::copy).orElse(null);
        this.status = filter.optionalStatus().map(TimeKeepingExplanationStatusFilter::copy).orElse(null);
        this.isActive = filter.isActive == null ? null : filter.isActive.copy();
    }

    @Override
    public TimeKeepingExplanationFilter copy() {
        return new TimeKeepingExplanationFilter(this);
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public void setIsActive(Boolean isActive) {
        isActive().setEquals(isActive);
    }

    public TimeKeepingExplanationTypeFilter getType() {
        return type;
    }

    public Optional<TimeKeepingExplanationTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TimeKeepingExplanationTypeFilter type() {
        if (type == null) {
            setType(new TimeKeepingExplanationTypeFilter());
        }
        return type;
    }

    public void setType(TimeKeepingExplanationTypeFilter type) {
        this.type = type;
    }

    public TimeKeepingExplanationStatusFilter getStatus() {
        return status;
    }

    public Optional<TimeKeepingExplanationStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TimeKeepingExplanationStatusFilter status() {
        if (status == null) {
            setStatus(new TimeKeepingExplanationStatusFilter());
        }
        return status;
    }

    public void setStatus(TimeKeepingExplanationStatusFilter status) {
        this.status = status;
    }

    public static class TimeKeepingExplanationTypeFilter extends Filter<TimeKeepingViolationType> {

        public TimeKeepingExplanationTypeFilter() {
        }

        public TimeKeepingExplanationTypeFilter(TimeKeepingExplanationTypeFilter filter) {
            super(filter);
        }

        @Override
        public TimeKeepingExplanationTypeFilter copy() {
            return new TimeKeepingExplanationTypeFilter(this);
        }
    }

    public static class TimeKeepingExplanationStatusFilter extends Filter<ExplanationStatus> {

        public TimeKeepingExplanationStatusFilter() {
        }

        public TimeKeepingExplanationStatusFilter(TimeKeepingExplanationStatusFilter filter) {
            super(filter);
        }

        @Override
        public TimeKeepingExplanationStatusFilter copy() {
            return new TimeKeepingExplanationStatusFilter(this);
        }
    }
}
