package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PurchaseRequestFile.
 */
@Data
@Table("purchase_request_file")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseRequestFile implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("file_path")
    private String filePath;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "purchaseRequestFiles", "purchaseDeliveries", "purchaseReviews" }, allowSetters = true)
    private PurchaseRequest purchaseRequest;

    @Column("purchase_request_id")
    private UUID purchaseRequestId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PurchaseRequestFile id(UUID id) {
        this.setId(id);
        return this;
    }

    public PurchaseRequestFile filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public PurchaseRequestFile setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public PurchaseRequestFile purchaseRequest(PurchaseRequest purchaseRequest) {
        this.setPurchaseRequest(purchaseRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
