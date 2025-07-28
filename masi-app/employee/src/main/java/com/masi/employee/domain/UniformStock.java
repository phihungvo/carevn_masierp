package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UniformStock.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("uniform_stock")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformStock implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("stock")
    private Integer stock;

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

    @Column("department")
    private String department;

    @Column("warehouse_id")
    private UUID warehouseId;

    @Column("warehouse_name")
    private String warehouseName;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "uniformFormDetails", "uniformStocks" }, allowSetters = true)
    private Uniform uniform;

    @Column("uniform_id")
    private UUID uniformId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UniformStock id(UUID id) {
        this.setId(id);
        return this;
    }

    public UniformStock stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public UniformStock createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public UniformStock createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public UniformStock updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public UniformStock updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public UniformStock deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public UniformStock deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public UniformStock company(String company) {
        this.setCompany(company);
        return this;
    }

    public UniformStock department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UniformStock setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public UniformStock uniform(Uniform uniform) {
        this.setUniform(uniform);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
