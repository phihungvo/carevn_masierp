package com.masi.production.service.dto;

import com.masi.production.domain.enumeration.QcSampleStatus;
import java.io.Serial;
import java.io.Serializable;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.StringFilter;

public class QuantityCheckFilter implements Serializable, Criteria {

    public static class QcSampleStatusFilter extends Filter<QcSampleStatus> {

        public QcSampleStatusFilter() {}

        public QcSampleStatusFilter(QcSampleStatusFilter filter) {
            super(filter);
        }

        @Override
        public QcSampleStatusFilter copy() {
            return new QcSampleStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private QcSampleStatusFilter status;

    private StringFilter search;
    private BooleanFilter isActive;

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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

    public QcSampleStatusFilter getStatus() {
        return status;
    }

    public StringFilter getSearch() {
        return search;
    }


    public StringFilter search() {
        if (search == null) {
            search = new StringFilter();
        }
        return search;
    }

    public QcSampleStatusFilter status() {
        if (status == null) {
            status = new QcSampleStatusFilter();
        }
        return status;
    }

    public void setStatus(QcSampleStatusFilter status) {
        this.status = status;
    }

    public void setSearch(StringFilter search) {
        this.search = search;
    }

    public void setStatus(QcSampleStatus status) {
        status().setEquals(status);
    }

    public void setSearch(String search) {
        search().setContains(search);
    }

    public QuantityCheckFilter copy() {
        return new QuantityCheckFilter(this);
    }

    public QuantityCheckFilter() {}

    public QuantityCheckFilter(QuantityCheckFilter other) {
        if (other.status != null) {
            this.status = other.status == null ? null : other.status.copy();
            this.search = other.search == null ? null : other.search.copy();
            this.isActive = other.isActive == null ? null : other.isActive.copy();
        }
    }

    @Override
    public String toString() {
        return (
            "QuantityCheckFilter{" +
            (status != null ? "status=" + status + ", " : "") +
            (search != null ? "search=" + search + ", " : "") +
            "}"
        );
    }
}
