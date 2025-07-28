package com.masi.logistics.domain;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
 * A Suppliers.
 */
@Data
@Table("suppliers")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Suppliers implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("birthday")
    private ZonedDateTime birthday;

    @Column("email")
    private String email;

    @Column("address")
    private String address;

    @Column("address_service")
    private String addressService;

    @Column("phone")
    private String phone;

    @Column("bank_info")
    private String bankInfo;

    @Column("tax_code")
    private String taxCode;

    @Column("contact")
    private String contact;

    @Column("payment_term")
    private ZonedDateTime paymentTerm;

    @Column("payment_term_number")
    private Integer paymentTermNumber;

    @Column("payment_term_text")
    private String paymentTermText;

    @Column("short_name")
    private String shortName;

    @Column("fax")
    private String fax;

    @Column("note")
    private String note;

    @Column("full_name")
    private String fullName;

    @Column("position")
    private String position;

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

    @Column("is_active")
    private Boolean isActive;

    @Column("attachment")
    private Json attachment;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "suppliers" }, allowSetters = true)
    private SupplierGroup supplierGroup;

    @Transient
    @JsonIgnoreProperties(value = { "suppliers" }, allowSetters = true)
    private SupplierType supplierType;

    @Column("supplier_type_id")
    private UUID supplierTypeId;

    @Column("supplier_group_id")
    private UUID supplierGroupId;

    @Column("manager_id")
    private UUID managerId;

    @Column("debt_employees")
    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json debtEmployees;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Suppliers id(UUID id) {
        this.setId(id);
        return this;
    }

    public Suppliers code(String code) {
        this.setCode(code);
        return this;
    }

    public Suppliers name(String name) {
        this.setName(name);
        return this;
    }

    public Suppliers email(String email) {
        this.setEmail(email);
        return this;
    }

    public Suppliers address(String address) {
        this.setAddress(address);
        return this;
    }

    public Suppliers phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public Suppliers note(String note) {
        this.setNote(note);
        return this;
    }

    public Suppliers createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public Suppliers createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public Suppliers updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public Suppliers updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public Suppliers deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public Suppliers deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public Suppliers company(String company) {
        this.setCompany(company);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Suppliers setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Suppliers supplierGroup(SupplierGroup supplierGroup) {
        this.setSupplierGroup(supplierGroup);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
