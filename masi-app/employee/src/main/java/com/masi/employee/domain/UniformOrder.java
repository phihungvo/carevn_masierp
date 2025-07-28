package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.UniformOrderStatus;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UniformOrder.
 */
@Data
@Table("uniform_order")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrder implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column(value = "code")
    private String code;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private Integer quantity;

    @Column("total_base_price")
    private Double totalBasePrice;

    @Column("total_actual_price")
    private Double totalActualPrice;

    @NotNull(message = "must not be null")
    @Column("date")
    private ZonedDateTime date;

    @NotNull(message = "must not be null")
    @Column("status")
    private UniformOrderStatus status;

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

    @Column("supplier_id")
    private UUID supplierId;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "uniform", "uniformRelease", "uniformOrder", "uniformReturn" }, allowSetters = true)
    private Set<UniformFormDetail> uniformFormDetails = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "uniformOrder" }, allowSetters = true)
    private Set<UniformOrderProcess> uniformOrderProcesses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UniformOrder id(UUID id) {
        this.setId(id);
        return this;
    }

    public UniformOrder name(String name) {
        this.setName(name);
        return this;
    }

    public UniformOrder quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public UniformOrder date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public UniformOrder status(UniformOrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public UniformOrder createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public UniformOrder createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public UniformOrder updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public UniformOrder updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public UniformOrder deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public UniformOrder deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public UniformOrder company(String company) {
        this.setCompany(company);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UniformOrder setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public UniformOrder uniformFormDetails(Set<UniformFormDetail> uniformFormDetails) {
        this.setUniformFormDetails(uniformFormDetails);
        return this;
    }

    public UniformOrder addUniformFormDetail(UniformFormDetail uniformFormDetail) {
        this.uniformFormDetails.add(uniformFormDetail);
        uniformFormDetail.setUniformOrder(this);
        return this;
    }

    public UniformOrder removeUniformFormDetail(UniformFormDetail uniformFormDetail) {
        this.uniformFormDetails.remove(uniformFormDetail);
        uniformFormDetail.setUniformOrder(null);
        return this;
    }

    public UniformOrder uniformOrderProcesses(Set<UniformOrderProcess> uniformOrderProcesses) {
        this.setUniformOrderProcesses(uniformOrderProcesses);
        return this;
    }

    public UniformOrder addUniformOrderProcess(UniformOrderProcess uniformOrderProcess) {
        this.uniformOrderProcesses.add(uniformOrderProcess);
        uniformOrderProcess.setUniformOrder(this);
        return this;
    }

    public UniformOrder removeUniformOrderProcess(UniformOrderProcess uniformOrderProcess) {
        this.uniformOrderProcesses.remove(uniformOrderProcess);
        uniformOrderProcess.setUniformOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
