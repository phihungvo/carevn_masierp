package com.masi.logistics.domain.criteria;

import com.masi.logistics.domain.enumeration.ContractStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.SupplierContract} entity. This class is used
 * in {@link com.masi.logistics.web.rest.SupplierContractResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /supplier-contracts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierContractCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContractStatus
     */
    public static class ContractStatusFilter extends Filter<ContractStatus> {

        public ContractStatusFilter() {}

        public ContractStatusFilter(ContractStatusFilter filter) {
            super(filter);
        }

        @Override
        public ContractStatusFilter copy() {
            return new ContractStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter contractCode;

    private StringFilter contractName;

    private UUIDFilter supplierId;

    private LocalDateFilter contractDate;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private StringFilter note;

    private StringFilter attachments;

    private ContractStatusFilter status;

    private StringFilter company;

    private StringFilter department;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdAt;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter deletedBy;

    private ZonedDateTimeFilter deletedAt;

    private UUIDFilter suppliesRequestId;

    private String search;

    private Boolean distinct;

    public SupplierContractCriteria() {}

    public SupplierContractCriteria(SupplierContractCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.contractCode = other.optionalContractCode().map(StringFilter::copy).orElse(null);
        this.contractName = other.optionalContractName().map(StringFilter::copy).orElse(null);
        this.supplierId = other.optionalSupplierId().map(UUIDFilter::copy).orElse(null);
        this.contractDate = other.optionalContractDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.attachments = other.optionalAttachments().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ContractStatusFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.search = other.optionalSearch().orElse(null);
        this.suppliesRequestId = other.optionalSuppliesRequestId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SupplierContractCriteria copy() {
        return new SupplierContractCriteria(this);
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public Optional<StringFilter> optionalContractCode() {
        return Optional.ofNullable(contractCode);
    }

    public StringFilter contractCode() {
        if (contractCode == null) {
            setContractCode(new StringFilter());
        }
        return contractCode;
    }

    public Optional<StringFilter> optionalContractName() {
        return Optional.ofNullable(contractName);
    }

    public StringFilter contractName() {
        if (contractName == null) {
            setContractName(new StringFilter());
        }
        return contractName;
    }

    public Optional<UUIDFilter> optionalSupplierId() {
        return Optional.ofNullable(supplierId);
    }

    public UUIDFilter supplierId() {
        if (supplierId == null) {
            setSupplierId(new UUIDFilter());
        }
        return supplierId;
    }

    public Optional<LocalDateFilter> optionalContractDate() {
        return Optional.ofNullable(contractDate);
    }

    public LocalDateFilter contractDate() {
        if (contractDate == null) {
            setContractDate(new LocalDateFilter());
        }
        return contractDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public Optional<StringFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public StringFilter note() {
        if (note == null) {
            setNote(new StringFilter());
        }
        return note;
    }

    public Optional<StringFilter> optionalAttachments() {
        return Optional.ofNullable(attachments);
    }

    public StringFilter attachments() {
        if (attachments == null) {
            setAttachments(new StringFilter());
        }
        return attachments;
    }

    public Optional<ContractStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ContractStatusFilter status() {
        if (status == null) {
            setStatus(new ContractStatusFilter());
        }
        return status;
    }

    public Optional<StringFilter> optionalCompany() {
        return Optional.ofNullable(company);
    }

    public StringFilter company() {
        if (company == null) {
            setCompany(new StringFilter());
        }
        return company;
    }

    public Optional<StringFilter> optionalDepartment() {
        return Optional.ofNullable(department);
    }

    public StringFilter department() {
        if (department == null) {
            setDepartment(new StringFilter());
        }
        return department;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
    }

    public Optional<StringFilter> optionalUpdatedBy() {
        return Optional.ofNullable(updatedBy);
    }

    public StringFilter updatedBy() {
        if (updatedBy == null) {
            setUpdatedBy(new StringFilter());
        }
        return updatedBy;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
    }

    public Optional<StringFilter> optionalDeletedBy() {
        return Optional.ofNullable(deletedBy);
    }

    public StringFilter deletedBy() {
        if (deletedBy == null) {
            setDeletedBy(new StringFilter());
        }
        return deletedBy;
    }

    public Optional<ZonedDateTimeFilter> optionalDeletedAt() {
        return Optional.ofNullable(deletedAt);
    }

    public ZonedDateTimeFilter deletedAt() {
        if (deletedAt == null) {
            setDeletedAt(new ZonedDateTimeFilter());
        }
        return deletedAt;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public Optional<String> optionalSearch() {
        return Optional.ofNullable(search);
    }

    public String search() {
        if (search == null) {
            setSearch(null);
        }
        return search;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public Optional<UUIDFilter> optionalSuppliesRequestId() {
        return Optional.ofNullable(suppliesRequestId);
    }

    public UUIDFilter suppliesRequestId() {
        if (suppliesRequestId == null) {
            setSuppliesRequestId(new UUIDFilter());
        }
        return suppliesRequestId;
    }

}
