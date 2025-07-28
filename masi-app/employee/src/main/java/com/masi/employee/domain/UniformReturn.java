package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.io.Serial;
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
 * A UniformReturn.
 */
@Data
@Table("uniform_return")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformReturn implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("date")
    private ZonedDateTime date;

    @Column("employee_id")
    private UUID employeeId;

    @Column("uniform_release_id")
    private UUID uniformReleaseId;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private Integer quantity;

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
    @JsonIgnoreProperties(value = {"uniform", "uniformRelease", "uniformOrder", "uniformReturn"}, allowSetters = true)
    private Set<UniformFormDetail> uniformFormDetails = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = {"uniformFormDetails", "uniformReturn"}, allowSetters = true)
    private UniformRelease uniformRelease;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UniformReturn id(UUID id) {
        this.setId(id);
        return this;
    }

    public UniformReturn date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public UniformReturn employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public UniformReturn quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public UniformReturn createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public UniformReturn createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public UniformReturn updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public UniformReturn updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public UniformReturn deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public UniformReturn deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public UniformReturn company(String company) {
        this.setCompany(company);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UniformReturn setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public UniformReturn uniformFormDetails(Set<UniformFormDetail> uniformFormDetails) {
        this.setUniformFormDetails(uniformFormDetails);
        return this;
    }

    public UniformReturn addUniformFormDetail(UniformFormDetail uniformFormDetail) {
        this.uniformFormDetails.add(uniformFormDetail);
        uniformFormDetail.setUniformReturn(this);
        return this;
    }

    public UniformReturn removeUniformFormDetail(UniformFormDetail uniformFormDetail) {
        this.uniformFormDetails.remove(uniformFormDetail);
        uniformFormDetail.setUniformReturn(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
