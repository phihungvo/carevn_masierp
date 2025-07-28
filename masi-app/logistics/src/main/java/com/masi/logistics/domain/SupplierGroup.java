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
 * A SupplierGroup.
 */
@Data
@Table("supplier_group")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierGroup implements Serializable, Persistable<UUID> {

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
    @JsonIgnoreProperties(value = { "supplierGroup" }, allowSetters = true)
    private Set<Suppliers> suppliers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SupplierGroup id(UUID id) {
        this.setId(id);
        return this;
    }

    public SupplierGroup name(String name) {
        this.setName(name);
        return this;
    }

    public SupplierGroup createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public SupplierGroup createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public SupplierGroup updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public SupplierGroup updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public SupplierGroup deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public SupplierGroup deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public SupplierGroup company(String company) {
        this.setCompany(company);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public SupplierGroup setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public SupplierGroup suppliers(Set<Suppliers> suppliers) {
        this.setSuppliers(suppliers);
        return this;
    }

    public SupplierGroup addSuppliers(Suppliers suppliers) {
        this.suppliers.add(suppliers);
        suppliers.setSupplierGroup(this);
        return this;
    }

    public SupplierGroup removeSuppliers(Suppliers suppliers) {
        this.suppliers.remove(suppliers);
        suppliers.setSupplierGroup(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
