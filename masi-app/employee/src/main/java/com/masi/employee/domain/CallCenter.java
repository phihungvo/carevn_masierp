package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.GroupCs;
import com.masi.employee.domain.enumeration.StatusEntity;
import com.masi.employee.domain.enumeration.TypeCS;
import com.masi.employee.domain.enumeration.TypePageCS;
import com.masi.employee.service.dto.CustomerDTO;
import com.masi.employee.service.dto.EmployeeProfileDTO;
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
 * A CallCenter.
 */
@Data
@Table("call_center")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CallCenter implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("reception_date")
    private ZonedDateTime receptionDate;

    @Column("group_cs")
    private GroupCs groupCS;

    @Column("phone_of_caller")
    private String phoneOfCaller;

    @Column("phone_of_name")
    private String phoneOfName;

    @Column("status")
    private StatusEntity status;

    @Column("type_cs")
    private TypeCS typeCS;

    @Column("customer_id")
    private UUID customerId;

    @Transient
    private CustomerDTO customer;

    @Column("type_page_cs")
    private TypePageCS typePageCs;

    @Column("employee_created_id")
    private UUID employeeCreatedId;

    @Transient
    private EmployeeProfileDTO employeeCreated;

    @Column("attribute")
    private Json attribute;

    @Column("attachment")
    private Json attachment;

    @Column("problem_content")
    private String problemContent;

    @Column("resolution_content")
    private String resolutionContent;

    @Column("response_content")
    private String responseContent;

    @Column("employee_assign_id")
    private UUID employeeAssignId;

    @Column("employee_assign_date")
    private ZonedDateTime employeeAssignDate;
    @Transient
    private EmployeeProfileDTO employeeAssign;

    @Column("employee_close_id")
    private UUID employeeCloseId;
    @Transient
    private EmployeeProfileDTO employeeClose;

    @Column("employee_close_date")
    private ZonedDateTime employeeCloseDate;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("confirm_date")
    private ZonedDateTime confirmDate;

    @Column("source_cs")
    private String sourceCs;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public CallCenter id(UUID id) {
        this.setId(id);
        return this;
    }

    public CallCenter code(String code) {
        this.setCode(code);
        return this;
    }

    public CallCenter receptionDate(ZonedDateTime receptionDate) {
        this.setReceptionDate(receptionDate);
        return this;
    }

    public CallCenter groupCS(GroupCs groupCS) {
        this.setGroupCS(groupCS);
        return this;
    }

    public CallCenter phoneOfCaller(String phoneOfCaller) {
        this.setPhoneOfCaller(phoneOfCaller);
        return this;
    }

    public CallCenter phoneOfName(String phoneOfName) {
        this.setPhoneOfName(phoneOfName);
        return this;
    }

    public CallCenter status(StatusEntity status) {
        this.setStatus(status);
        return this;
    }

    public CallCenter typeCS(TypeCS typeCS) {
        this.setTypeCS(typeCS);
        return this;
    }

    public CallCenter customerId(UUID customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public CallCenter employeeCreatedId(UUID employeeCreatedId) {
        this.setEmployeeCreatedId(employeeCreatedId);
        return this;
    }

    public CallCenter attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public CallCenter problemContent(String problemContent) {
        this.setProblemContent(problemContent);
        return this;
    }

    public CallCenter resolutionContent(String resolutionContent) {
        this.setResolutionContent(resolutionContent);
        return this;
    }

    public CallCenter responseContent(String responseContent) {
        this.setResponseContent(responseContent);
        return this;
    }

    public CallCenter employeeAssignId(UUID employeeAssignId) {
        this.setEmployeeAssignId(employeeAssignId);
        return this;
    }

    public CallCenter employeeCloseId(UUID employeeCloseId) {
        this.setEmployeeCloseId(employeeCloseId);
        return this;
    }

    public CallCenter isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public CallCenter createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public CallCenter createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public CallCenter updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public CallCenter updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public CallCenter deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public CallCenter deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public CallCenter company(String company) {
        this.setCompany(company);
        return this;
    }

    public CallCenter department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public CallCenter setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
