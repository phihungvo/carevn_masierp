package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.domain.enumeration.ContractStatus;
import com.masi.sale.domain.enumeration.ContractType;
import com.masi.sale.service.dto.ContractDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Contract.
 */
@Data
@Table("contract")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("old_status")
    private ContractStatus oldStatus;

    @NotNull(message = "must not be null")
    @Column("status")
    private ContractStatus status;

    @NotNull(message = "must not be null")
    @Column("contract_name")
    private String contractName;

    @NotNull(message = "must not be null")
    @Column("contract_valid_from")
    private LocalDate contractValidFrom;

    @Column("quotation_id")
    private UUID quotationId;

    @NotNull(message = "must not be null")
    @Column("contract_valid_to")
    private LocalDate contractValidTo;

    @Column("customer_id")
    private UUID customerId;

    @Transient
    private Customer customer;

    @NotNull(message = "must not be null")
    @Column("contract_type")
    private ContractType contractType;

    @NotNull(message = "must not be null")
    @Column("contract_owner")
    private UUID contractOwner;

    @NotNull(message = "must not be null")
    @Column("contract_total")
    private BigDecimal contractTotal;

    @NotNull(message = "must not be null")
    @Column("protein_percent")
    private String proteinPercent;

    @Column("approval_sign_file")
    private String approvalSignFile;

    @Column("review_by")
    private UUID reviewBy;

    @Column("approval_sign_name")
    private String approvalSignFileName;

    @Column("review_at")
    private ZonedDateTime reviewAt;

    @Column("reject_note")
    private String rejectNote;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Column("delivery_term_from")
    private LocalDate deliveryTermFrom;

    @Column("delivery_term_to")
    private LocalDate deliveryTermTo;

    @Column("pay_term")
    private String payTerm;

    @Column("pay_condition")
    private String payCondition;

    @Column("delivery_location")
    private String deliveryLocation;

    @Transient
    private boolean isPersisted;
    @Column("department")
    private String department;

    @Column("company")
    private String company;
    @Transient
    @JsonIgnoreProperties(value = {"contract"}, allowSetters = true)
    private Set<ContractFile> contractFiles = new HashSet<>();

    @Column("monetary_unit")
    private String monetaryUnit;

    @Column("exchange_rate")
    private Float exchangeRate;


    // jhipster-needle-entity-add-field - JHipster will add fields here


    public Contract id(UUID id) {
        this.setId(id);
        return this;
    }

    public Contract status(ContractStatus status) {
        this.setStatus(status);
        return this;
    }

    public Contract contractName(String contractName) {
        this.setContractName(contractName);
        return this;
    }


    public Contract contractType(ContractType contractType) {
        this.setContractType(contractType);
        return this;
    }

    public Contract contractOwner(UUID contractOwner) {
        this.setContractOwner(contractOwner);
        return this;
    }

    public Contract contractTotal(BigDecimal contractTotal) {
        this.setContractTotal(contractTotal);
        return this;
    }

    public Contract proteinPercent(String proteinPercent) {
        this.setProteinPercent(proteinPercent);
        return this;
    }


    public Contract rejectNote(String rejectNote) {
        this.setRejectNote(rejectNote);
        return this;
    }

    public Contract lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public Contract createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Contract isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        this.setIsActive(!isDeleted); // Ensure isActive is set correctly
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Contract setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Contract contractFiles(Set<ContractFile> contractFiles) {
        this.setContractFiles(contractFiles);
        return this;
    }

    public Contract addContractFile(ContractFile contractFile) {
        this.contractFiles.add(contractFile);
        contractFile.setContract(this);
        return this;
    }

    public Contract removeContractFile(ContractFile contractFile) {
        this.contractFiles.remove(contractFile);
        contractFile.setContract(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    public ContractDTO toDto() {
        ContractDTO dto = this.toBriefDTO();
        dto.setApprovalSignFile(this.approvalSignFile);
        return dto;
    }

    public ContractDTO toBriefDTO() {
        ContractDTO dto = new ContractDTO();
        dto.setId(this.id);
        if (customer != null) {
            dto.setCustomer(this.customer.toDto());
        }
        dto.setReviewAt(this.reviewAt);
        dto.setStatus(this.status);
        dto.setContractName(this.contractName);
        dto.setContractValidFrom(this.contractValidFrom);
        dto.setContractValidTo(this.contractValidTo);
        dto.setCustomerId(this.customerId);
        dto.setContractType(this.contractType);
        dto.setContractOwner(this.contractOwner);
        dto.setContractTotal(this.contractTotal);
        dto.setProteinPercent(this.proteinPercent);
        dto.setLastUpdated(this.lastUpdated);
        dto.setCreatedDate(this.createdDate);
        dto.setIsDeleted(this.isDeleted);
        dto.setIsActive(this.isActive);
        if (Boolean.FALSE.equals(this.isActive)) {
            dto.setStatus(ContractStatus.DELETED);
        }
        dto.setDeliveryTermFrom(this.deliveryTermFrom);
        dto.setDeliveryTermTo(this.deliveryTermTo);
        dto.setPayTerm(this.payTerm);
        dto.setPayCondition(this.payCondition);
        dto.setDeliveryLocation(this.deliveryLocation);

        dto.setMonetaryUnit(this.monetaryUnit);
        dto.setExchangeRate(this.exchangeRate);
        dto.setOldStatus(this.oldStatus);
        dto.setApprovalSignFile(this.approvalSignFile);
        dto.setApprovalSignFileName(this.approvalSignFileName);
        dto.setRejectNote(this.rejectNote);
        dto.setReviewBy(this.reviewBy);
        dto.setQuotationId(this.quotationId);

        return dto;
    }


}
