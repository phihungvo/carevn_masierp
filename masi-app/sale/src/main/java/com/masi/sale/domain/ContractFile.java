package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ContractFile.
 */
@Data
@Table("contract_file")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractFile implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("contract_file_path")
    private String contractFilePath;

    @NotNull(message = "must not be null")
    @Column("contract_index_file_path")
    private String contractIndexFilePath;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "contractFiles" }, allowSetters = true)
    private Contract contract;

    @Column("contract_id")
    private UUID contractId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ContractFile id(UUID id) {
        this.setId(id);
        return this;
    }

    public ContractFile contractFilePath(String contractFilePath) {
        this.setContractFilePath(contractFilePath);
        return this;
    }

    public ContractFile contractIndexFilePath(String contractIndexFilePath) {
        this.setContractIndexFilePath(contractIndexFilePath);
        return this;
    }

    public ContractFile lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public ContractFile createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public ContractFile isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ContractFile setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public ContractFile contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
