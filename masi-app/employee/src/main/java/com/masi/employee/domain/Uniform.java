package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.service.dto.UniformDTO;
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
 * A Uniform.
 */
@Data
@Table("uniform")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Uniform implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("base_price")
    private Double basePrice;

    @Column("uom_group_id")
    private UUID uomGroupId;

    @Column("uom_id")
    private UUID uomId;

    @Column("status")
    private String status = "ENABLE";

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

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = {"uniform", "uniformRelease", "uniformOrder", "uniformReturn"}, allowSetters = true)
    private Set<UniformFormDetail> uniformFormDetails = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = {"uniform"}, allowSetters = true)
    private Set<UniformStock> uniformStocks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Uniform id(UUID id) {
        this.setId(id);
        return this;
    }

    public Uniform name(String name) {
        this.setName(name);
        return this;
    }

    public Uniform createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public Uniform createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public Uniform updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public Uniform updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public Uniform deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public Uniform deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public Uniform company(String company) {
        this.setCompany(company);
        return this;
    }

    public Uniform department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Uniform setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Uniform uniformFormDetails(Set<UniformFormDetail> uniformFormDetails) {
        this.setUniformFormDetails(uniformFormDetails);
        return this;
    }

    public Uniform addUniformFormDetail(UniformFormDetail uniformFormDetail) {
        this.uniformFormDetails.add(uniformFormDetail);
        uniformFormDetail.setUniform(this);
        return this;
    }

    public Uniform removeUniformFormDetail(UniformFormDetail uniformFormDetail) {
        this.uniformFormDetails.remove(uniformFormDetail);
        uniformFormDetail.setUniform(null);
        return this;
    }

    public Uniform uniformStocks(Set<UniformStock> uniformStocks) {
        this.setUniformStocks(uniformStocks);
        return this;
    }

    public Uniform addUniformStock(UniformStock uniformStock) {
        this.uniformStocks.add(uniformStock);
        uniformStock.setUniform(this);
        return this;
    }

    public Uniform removeUniformStock(UniformStock uniformStock) {
        this.uniformStocks.remove(uniformStock);
        uniformStock.setUniform(null);
        return this;
    }

    public UniformDTO toDto() {
        UniformDTO dto = new UniformDTO();
        dto.setId(this.getId());
        dto.setCode(this.getCode());
        dto.setName(this.getName());
        dto.setCreateAt(this.getCreateAt());
        dto.setCreateBy(this.getCreateBy());
        dto.setUpdateAt(this.getUpdateAt());
        dto.setUpdateBy(this.getUpdateBy());
        dto.setDeleteAt(this.getDeleteAt());
        dto.setDeleteBy(this.getDeleteBy());
        dto.setCompany(this.getCompany());
        dto.setBasePrice(this.getBasePrice());
        dto.setUomId(this.getUomId());
        dto.setDepartment(this.getDepartment());
        dto.setStatus(this.getStatus());
        return dto;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
