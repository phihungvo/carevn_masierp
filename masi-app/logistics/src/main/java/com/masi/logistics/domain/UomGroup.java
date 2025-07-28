package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
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
 * A UomGroup.
 */
@Data
@Table("uom_group")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UomGroup implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("create_at")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    @Column("create_by")
    private String createBy;

    @Column("update_at")
    private ZonedDateTime updateAt;

    @Column("update_by")
    private String updateBy;

    @Column("delete_at")
    private ZonedDateTime deleteAt;

    @Column("delete_by")
    private String deleteBy;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "baseUom", "altUom", "uomGroup" }, allowSetters = true)
    private Set<UomGroupDetails> uomGroupDetails = new HashSet<>();

    @Transient
    private Uom baseUom;

    @Column("base_uom_id")
    private UUID baseUomId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UomGroup id(UUID id) {
        this.setId(id);
        return this;
    }

    public UomGroup name(String name) {
        this.setName(name);
        return this;
    }

    public UomGroup createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public UomGroup createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public UomGroup updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public UomGroup updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public UomGroup deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public UomGroup deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public UomGroup company(String company) {
        this.setCompany(company);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UomGroup setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public UomGroup uomGroupDetails(Set<UomGroupDetails> uomGroupDetails) {
        this.setUomGroupDetails(uomGroupDetails);
        return this;
    }

    public UomGroup addUomGroupDetails(UomGroupDetails uomGroupDetails) {
        this.uomGroupDetails.add(uomGroupDetails);
        uomGroupDetails.setUomGroup(this);
        return this;
    }

    public UomGroup removeUomGroupDetails(UomGroupDetails uomGroupDetails) {
        this.uomGroupDetails.remove(uomGroupDetails);
        uomGroupDetails.setUomGroup(null);
        return this;
    }

    public UomGroup baseUom(Uom uom) {
        this.setBaseUom(uom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
