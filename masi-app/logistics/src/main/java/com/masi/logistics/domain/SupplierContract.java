package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.enumeration.ContractStatus;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SupplierContract.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("supplier_contract")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierContract  extends AbstractAuditingEntity<UUID> implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    public static final String ENTITY_NAME = "supplierContract";

    public static final String DEFAULT_LIQUIDATION_REQUEST_APPROVAL_GROUP = "LIQUIDATION";

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("contract_code")
    private String contractCode;

    @Column("contract_name")
    private String contractName;

    @Column("supplier_id")
    private UUID supplierId;

    @Transient
    @JsonIgnoreProperties(value = { "supplierContract" }, allowSetters = true)
    private Suppliers supplier;

    @Column("contract_date")
    private LocalDate contractDate;

    @Column("end_date")
    private LocalDate endDate;

    @Column("note")
    private String note;

    @Column("attachments")
    private Json attachments;

    @Column("status")
    private ContractStatus status;

    @Column("contract_amount")
    private BigDecimal contractAmount;

    @Column("supplies_request_id")
    private UUID suppliesRequestId;

    @Transient
    @JsonIgnoreProperties(value = { "supplierContract" }, allowSetters = true)
    private SuppliesRequest suppliesRequest;

    @Column("payment_term_number")
    private Double paymentTermNumber;

    @Column("start_date")
    private LocalDate startDate;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("total_amount_after_vat")
    private BigDecimal totalAmountAfterVat;

    @Column("total_quantity")
    private BigDecimal totalQuantity;

    @Column("supplier_full_name")
    private String supplierFullName;

    @Column("supplier_position")
    private String supplierPosition;

    @Column("supplier_phone")
    private String supplierPhone;

    @Column("supplier_email")
    private String supplierEmail;

    @Column("delivery_est_date")
    private LocalDate deliveryEstDate;

    @Column("delivery_status")
    private ContractStatus.DeliveryStatus deliveryStatus;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "supplierContract" }, allowSetters = true)
    private Set<SupplierContractDetail> supplierContractDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SupplierContract id(UUID id) {
        this.setId(id);
        return this;
    }

    public SupplierContract contractCode(String contractCode) {
        this.setContractCode(contractCode);
        return this;
    }

    public SupplierContract contractName(String contractName) {
        this.setContractName(contractName);
        return this;
    }

    public SupplierContract supplierId(UUID supplierId) {
        this.setSupplierId(supplierId);
        return this;
    }

    public SupplierContract contractDate(LocalDate contractDate) {
        this.setContractDate(contractDate);
        return this;
    }

    public SupplierContract endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public SupplierContract note(String note) {
        this.setNote(note);
        return this;
    }

    public SupplierContract attachments(Json attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public SupplierContract status(ContractStatus status) {
        this.setStatus(status);
        return this;
    }

    public SupplierContract company(String company) {
        this.setCompany(company);
        return this;
    }

    public SupplierContract department(String department) {
        this.setDepartment(department);
        return this;
    }

    public SupplierContract createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public SupplierContract createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public SupplierContract updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public SupplierContract updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public SupplierContract deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public SupplierContract deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public SupplierContract setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public SupplierContract supplierContractDetails(Set<SupplierContractDetail> supplierContractDetails) {
        this.setSupplierContractDetails(supplierContractDetails);
        return this;
    }

    public SupplierContract addSupplierContractDetail(SupplierContractDetail supplierContractDetail) {
        this.supplierContractDetails.add(supplierContractDetail);
        supplierContractDetail.setSupplierContract(this);
        return this;
    }

    public SupplierContract removeSupplierContractDetail(SupplierContractDetail supplierContractDetail) {
        this.supplierContractDetails.remove(supplierContractDetail);
        supplierContractDetail.setSupplierContract(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
