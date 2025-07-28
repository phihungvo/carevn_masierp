package com.carevn.masi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Company.
 */
@Data
@Table("company")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Company implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("normalized_name")
    private String normalizedName;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("parent_id")
    private UUID parentId;

    @Column("code")
    private String code;

    @Column("tax_code")
    private String taxCode;

    @Column("website")
    private String website;

    @Column("callcenter")
    private String callcenter;

    @Column("address")
    private String address;

    @Column("representative_name")
    private String representativeName;

    @Column("representative_phone")
    private String representativePhone;

    @Column("representative_email")
    private String representativeEmail;

    @Column("representative_dob")
    private LocalDate representativeDob;

    @Column("representative_id_number")
    private String representativeIdNumber;

    @Column("image_id")
    private UUID imageId;

    @Column("is_deleted")
    private Boolean isDeleted = false;

    @Column("is_activated")
    private Boolean isActivated;

    @Transient
    private boolean isPersisted;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Company id(UUID id) {
        this.setId(id);
        return this;
    }

    public Company name(String name) {
        this.setName(name);
        return this;
    }

    public Company description(String description) {
        this.setDescription(description);
        return this;
    }

    public Company parentId(UUID parentId) {
        this.setParentId(parentId);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Company setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public boolean isPersisted() {
        return isPersisted;
    }

    public void setPersisted(boolean isPersisted) {
        this.isPersisted = isPersisted;
    }

}
