package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.TypeRequestApproval;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TypeRequestApprovalPages.
 */
@Data
@Table("type_request_approval_pages")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeRequestApprovalPages implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("page_name")
    private String pageName;

    @Column("type")
    private TypeRequestApproval type;

    @Column("number_of_reviewers")
    private Integer numberOfReviewers;


    @Column("is_department")
    private Boolean isDepartment;

    // Xem coi có cần chỉ định người duyệt không
    @Column("is_nominate")
    private Boolean isNominate;

    /*
            Khi nào TypeRequestApproval == Sequentially hoặc isNominate == true  thì mới vô đây
        Json
        {
           "employees": ['uuid1', 'uuid2', 'uuid3'],
        }
     */
    @Column("note")
    private Json note;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_by")
    private String createdBy;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public TypeRequestApprovalPages id(UUID id) {
        this.setId(id);
        return this;
    }

    public TypeRequestApprovalPages pageName(String pageName) {
        this.setPageName(pageName);
        return this;
    }

    public TypeRequestApprovalPages type(TypeRequestApproval type) {
        this.setType(type);
        return this;
    }

    public TypeRequestApprovalPages numberOfReviewers(Integer numberOfReviewers) {
        this.setNumberOfReviewers(numberOfReviewers);
        return this;
    }

    public TypeRequestApprovalPages company(String company) {
        this.setCompany(company);
        return this;
    }

    public TypeRequestApprovalPages department(String department) {
        this.setDepartment(department);
        return this;
    }

    public TypeRequestApprovalPages isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public TypeRequestApprovalPages createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public TypeRequestApprovalPages createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public TypeRequestApprovalPages updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public TypeRequestApprovalPages updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public TypeRequestApprovalPages deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public TypeRequestApprovalPages deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public TypeRequestApprovalPages setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
