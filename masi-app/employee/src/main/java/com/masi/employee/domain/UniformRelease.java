package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.UniformReleaseType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serial;
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
 * A UniformRelease.
 */
@Data
@Table("uniform_release")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformRelease implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @NotNull(message = "must not be null")
    @Column("date")
    private ZonedDateTime date;

    @NotNull(message = "must not be null")
    @Column("employee_id")
    private UUID employeeId;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private Integer quantity;

    @Column("note")
    private String note;

    @Column("file_id")
    private String fileId;

    @Column("file_name")
    private String fileName;

    @Column("signature_content_type")
    private String signatureContentType;

    @NotNull(message = "must not be null")
    @Column("type")
    private UniformReleaseType type;

    @NotNull(message = "must not be null")
    @Column("cost")
    private Float cost = 0F;

    @NotNull(message = "must not be null")
    @Column("is_returned")
    private Boolean isReturned;

    @Column("warehouse_id")
    private UUID warehouseId;

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

    @Column("remaining")
    private Integer remaining = 0;

    @Transient
    @JsonIgnoreProperties(value = { "uniform", "uniformRelease", "uniformOrder", "uniformReturn" }, allowSetters = true)
    private Set<UniformFormDetail> uniformFormDetails = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UniformRelease id(UUID id) {
        this.setId(id);
        return this;
    }

    public UniformRelease date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public UniformRelease employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public UniformRelease quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public UniformRelease note(String note) {
        this.setNote(note);
        return this;
    }

    public UniformRelease fileId(String fileId) {
        this.setFileId(fileId);
        return this;
    }

    public UniformRelease signatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
        return this;
    }

    public UniformRelease type(UniformReleaseType type) {
        this.setType(type);
        return this;
    }

    public UniformRelease cost(Float cost) {
        this.setCost(cost);
        return this;
    }

    public UniformRelease isReturned(Boolean isReturned) {
        this.setIsReturned(isReturned);
        return this;
    }

    public UniformRelease createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public UniformRelease createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public UniformRelease updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public UniformRelease updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public UniformRelease deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public UniformRelease deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public UniformRelease company(String company) {
        this.setCompany(company);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UniformRelease setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public UniformRelease uniformFormDetails(Set<UniformFormDetail> uniformFormDetails) {
        this.setUniformFormDetails(uniformFormDetails);
        return this;
    }

    public UniformRelease addUniformFormDetail(UniformFormDetail uniformFormDetail) {
        this.uniformFormDetails.add(uniformFormDetail);
        uniformFormDetail.setUniformRelease(this);
        return this;
    }

    public UniformRelease removeUniformFormDetail(UniformFormDetail uniformFormDetail) {
        this.uniformFormDetails.remove(uniformFormDetail);
        uniformFormDetail.setUniformRelease(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
