package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UniformOrderStock.
 */
@Data
@Table("uniform_order_stock")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrderStock implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("uniform_order_id")
    private UUID uniformOrderId;

    @Transient
    @JsonIgnoreProperties(value = { "uniformOrderStock" }, allowSetters = true)
    private UniformOrder uniformOrder;

    @Column("total_quantity")
    private Integer totalQuantity;

    @Column("warehouse_id")
    private UUID warehouseId;

    @Transient
    @JsonIgnoreProperties(value = { "uniformOrderStock" }, allowSetters = true)
    private Set<UniformFormDetail> uniformFormDetail;

    @NotNull(message = "must not be null")
    @Column("create_at")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    @Column("create_by")
    private String createBy;

    @Transient
    @JsonIgnoreProperties(value = { "uniformOrderStock" }, allowSetters = true)
    private Employee createdByProfile;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UniformOrderStock id(UUID id) {
        this.setId(id);
        return this;
    }

    public UniformOrderStock totalQuantity(Integer totalQuantity) {
        this.setTotalQuantity(totalQuantity);
        return this;
    }

    public UniformOrderStock code(String code) {
        this.setCode(code);
        return this;
    }

    public UniformOrderStock uniformOrderId(UUID uniformOrderId) {
        this.setUniformOrderId(uniformOrderId);
        return this;
    }

    public UniformOrderStock createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public UniformOrderStock createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public UniformOrderStock updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public UniformOrderStock updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public UniformOrderStock deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public UniformOrderStock deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public UniformOrderStock company(String company) {
        this.setCompany(company);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UniformOrderStock setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
