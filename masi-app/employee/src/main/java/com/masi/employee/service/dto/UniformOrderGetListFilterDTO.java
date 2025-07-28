package com.masi.employee.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

import java.io.Serializable;

import com.masi.employee.domain.enumeration.UniformOrderStatus;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformOrder} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrderGetListFilterDTO implements Serializable, Criteria {

    public static class UniformOrderStatusFilter extends Filter<UniformOrderStatus> {

        public UniformOrderStatusFilter() {
        }

        public UniformOrderStatusFilter(UniformOrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public UniformOrderStatusFilter copy() {
            return new UniformOrderStatusFilter(this);
        }
    }

    private StringFilter name;

    private ZonedDateTimeFilter startDate;

    private ZonedDateTimeFilter endDate;

    private UniformOrderStatusFilter status;

    private StringFilter deletedBy;
    private ZonedDateTimeFilter deletedAt;

    private StringFilter company;

    public UniformOrderGetListFilterDTO(UniformOrderGetListFilterDTO filterDTO) {
        if (filterDTO != null) {
            this.name = filterDTO.getName() == null ? null : filterDTO.getName().copy();
            this.startDate = filterDTO.getStartDate() == null ? null : filterDTO.getStartDate().copy();
            this.endDate = filterDTO.getEndDate() == null ? null : filterDTO.getEndDate().copy();
            this.status = filterDTO.getStatus() == null ? null : filterDTO.getStatus().copy();
            this.deletedBy = filterDTO.getDeletedBy() == null ? null : filterDTO.getDeletedBy().copy();
            this.deletedAt = filterDTO.getDeletedAt() == null ? null : filterDTO.getDeletedAt().copy();
            this.company = filterDTO.getCompany() == null ? null : filterDTO.getCompany().copy();
        }
    }

    public UniformOrderGetListFilterDTO copy() {
        return new UniformOrderGetListFilterDTO();
    }

    public StringFilter company() {
        if (company == null) {
            company = new StringFilter();
        }
        return company;
    }

    public StringFilter deletedBy() {
        if (deletedBy == null) {
            deletedBy = new StringFilter();
        }
        return deletedBy;
    }

    public ZonedDateTimeFilter deletedAt() {
        if (deletedAt == null) {
            deletedAt = new ZonedDateTimeFilter();
        }
        return deletedAt;
    }

    public ZonedDateTimeFilter startDate() {
        if (startDate == null) {
            startDate = new ZonedDateTimeFilter();
        }
        return startDate;
    }

    public ZonedDateTimeFilter endDate() {
        if (endDate == null) {
            endDate = new ZonedDateTimeFilter();
        }
        return endDate;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public UniformOrderStatusFilter status() {
        if (status == null) {
            status = new UniformOrderStatusFilter();
        }
        return status;
    }

}
