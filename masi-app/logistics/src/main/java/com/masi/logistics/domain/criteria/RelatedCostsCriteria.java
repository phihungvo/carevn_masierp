package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.RelatedCosts} entity. This class is used
 * in {@link com.masi.logistics.web.rest.RelatedCostsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /related-costs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RelatedCostsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter invoiceId;

    private UUIDFilter paymentMethodId;

    private StringFilter paymentMethodCode;

    private StringFilter paymentMethodName;

    private UUIDFilter vatId;

    private DoubleFilter vat;

    private BigDecimalFilter vatAmount;

    private BigDecimalFilter totalAmount;

    private DoubleFilter debtDays;

    private StringFilter note;

    private BooleanFilter isDeleted;

    private ZonedDateTimeFilter createdAt;

    private StringFilter createdBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter deletedAt;

    private StringFilter deletedBy;

    private StringFilter company;

    private StringFilter department;

    private Boolean distinct;

    public RelatedCostsCriteria() {}

    public RelatedCostsCriteria(RelatedCostsCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.invoiceId = other.optionalInvoiceId().map(UUIDFilter::copy).orElse(null);
        this.paymentMethodId = other.optionalPaymentMethodId().map(UUIDFilter::copy).orElse(null);
        this.paymentMethodCode = other.optionalPaymentMethodCode().map(StringFilter::copy).orElse(null);
        this.paymentMethodName = other.optionalPaymentMethodName().map(StringFilter::copy).orElse(null);
        this.vatId = other.optionalVatId().map(UUIDFilter::copy).orElse(null);
        this.vat = other.optionalVat().map(DoubleFilter::copy).orElse(null);
        this.vatAmount = other.optionalVatAmount().map(BigDecimalFilter::copy).orElse(null);
        this.totalAmount = other.optionalTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.debtDays = other.optionalDebtDays().map(DoubleFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RelatedCostsCriteria copy() {
        return new RelatedCostsCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
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

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public UUIDFilter getInvoiceId() {
        return invoiceId;
    }

    public Optional<UUIDFilter> optionalInvoiceId() {
        return Optional.ofNullable(invoiceId);
    }

    public UUIDFilter invoiceId() {
        if (invoiceId == null) {
            setInvoiceId(new UUIDFilter());
        }
        return invoiceId;
    }

    public void setInvoiceId(UUIDFilter invoiceId) {
        this.invoiceId = invoiceId;
    }

    public UUIDFilter getPaymentMethodId() {
        return paymentMethodId;
    }

    public Optional<UUIDFilter> optionalPaymentMethodId() {
        return Optional.ofNullable(paymentMethodId);
    }

    public UUIDFilter paymentMethodId() {
        if (paymentMethodId == null) {
            setPaymentMethodId(new UUIDFilter());
        }
        return paymentMethodId;
    }

    public void setPaymentMethodId(UUIDFilter paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public StringFilter getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public Optional<StringFilter> optionalPaymentMethodCode() {
        return Optional.ofNullable(paymentMethodCode);
    }

    public StringFilter paymentMethodCode() {
        if (paymentMethodCode == null) {
            setPaymentMethodCode(new StringFilter());
        }
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(StringFilter paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public StringFilter getPaymentMethodName() {
        return paymentMethodName;
    }

    public Optional<StringFilter> optionalPaymentMethodName() {
        return Optional.ofNullable(paymentMethodName);
    }

    public StringFilter paymentMethodName() {
        if (paymentMethodName == null) {
            setPaymentMethodName(new StringFilter());
        }
        return paymentMethodName;
    }

    public void setPaymentMethodName(StringFilter paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public UUIDFilter getVatId() {
        return vatId;
    }

    public Optional<UUIDFilter> optionalVatId() {
        return Optional.ofNullable(vatId);
    }

    public UUIDFilter vatId() {
        if (vatId == null) {
            setVatId(new UUIDFilter());
        }
        return vatId;
    }

    public void setVatId(UUIDFilter vatId) {
        this.vatId = vatId;
    }

    public DoubleFilter getVat() {
        return vat;
    }

    public Optional<DoubleFilter> optionalVat() {
        return Optional.ofNullable(vat);
    }

    public DoubleFilter vat() {
        if (vat == null) {
            setVat(new DoubleFilter());
        }
        return vat;
    }

    public void setVat(DoubleFilter vat) {
        this.vat = vat;
    }

    public BigDecimalFilter getVatAmount() {
        return vatAmount;
    }

    public Optional<BigDecimalFilter> optionalVatAmount() {
        return Optional.ofNullable(vatAmount);
    }

    public BigDecimalFilter vatAmount() {
        if (vatAmount == null) {
            setVatAmount(new BigDecimalFilter());
        }
        return vatAmount;
    }

    public void setVatAmount(BigDecimalFilter vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public Optional<BigDecimalFilter> optionalTotalAmount() {
        return Optional.ofNullable(totalAmount);
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            setTotalAmount(new BigDecimalFilter());
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public DoubleFilter getDebtDays() {
        return debtDays;
    }

    public Optional<DoubleFilter> optionalDebtDays() {
        return Optional.ofNullable(debtDays);
    }

    public DoubleFilter debtDays() {
        if (debtDays == null) {
            setDebtDays(new DoubleFilter());
        }
        return debtDays;
    }

    public void setDebtDays(DoubleFilter debtDays) {
        this.debtDays = debtDays;
    }

    public StringFilter getNote() {
        return note;
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

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
    }

    public Optional<BooleanFilter> optionalIsDeleted() {
        return Optional.ofNullable(isDeleted);
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            setIsDeleted(new BooleanFilter());
        }
        return isDeleted;
    }

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
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

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
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

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
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

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public StringFilter getUpdatedBy() {
        return updatedBy;
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

    public void setUpdatedBy(StringFilter updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTimeFilter getDeletedAt() {
        return deletedAt;
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

    public void setDeletedAt(ZonedDateTimeFilter deletedAt) {
        this.deletedAt = deletedAt;
    }

    public StringFilter getDeletedBy() {
        return deletedBy;
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

    public void setDeletedBy(StringFilter deletedBy) {
        this.deletedBy = deletedBy;
    }

    public StringFilter getCompany() {
        return company;
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

    public void setCompany(StringFilter company) {
        this.company = company;
    }

    public StringFilter getDepartment() {
        return department;
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

    public void setDepartment(StringFilter department) {
        this.department = department;
    }

    public Boolean getDistinct() {
        return distinct;
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

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RelatedCostsCriteria that = (RelatedCostsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(invoiceId, that.invoiceId) &&
            Objects.equals(paymentMethodId, that.paymentMethodId) &&
            Objects.equals(paymentMethodCode, that.paymentMethodCode) &&
            Objects.equals(paymentMethodName, that.paymentMethodName) &&
            Objects.equals(vatId, that.vatId) &&
            Objects.equals(vat, that.vat) &&
            Objects.equals(vatAmount, that.vatAmount) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(debtDays, that.debtDays) &&
            Objects.equals(note, that.note) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(deletedAt, that.deletedAt) &&
            Objects.equals(deletedBy, that.deletedBy) &&
            Objects.equals(company, that.company) &&
            Objects.equals(department, that.department) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            invoiceId,
            paymentMethodId,
            paymentMethodCode,
            paymentMethodName,
            vatId,
            vat,
            vatAmount,
            totalAmount,
            debtDays,
            note,
            isDeleted,
            createdAt,
            createdBy,
            updatedAt,
            updatedBy,
            deletedAt,
            deletedBy,
            company,
            department,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RelatedCostsCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalInvoiceId().map(f -> "invoiceId=" + f + ", ").orElse("") +
            optionalPaymentMethodId().map(f -> "paymentMethodId=" + f + ", ").orElse("") +
            optionalPaymentMethodCode().map(f -> "paymentMethodCode=" + f + ", ").orElse("") +
            optionalPaymentMethodName().map(f -> "paymentMethodName=" + f + ", ").orElse("") +
            optionalVatId().map(f -> "vatId=" + f + ", ").orElse("") +
            optionalVat().map(f -> "vat=" + f + ", ").orElse("") +
            optionalVatAmount().map(f -> "vatAmount=" + f + ", ").orElse("") +
            optionalTotalAmount().map(f -> "totalAmount=" + f + ", ").orElse("") +
            optionalDebtDays().map(f -> "debtDays=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalIsDeleted().map(f -> "isDeleted=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalUpdatedBy().map(f -> "updatedBy=" + f + ", ").orElse("") +
            optionalDeletedAt().map(f -> "deletedAt=" + f + ", ").orElse("") +
            optionalDeletedBy().map(f -> "deletedBy=" + f + ", ").orElse("") +
            optionalCompany().map(f -> "company=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
