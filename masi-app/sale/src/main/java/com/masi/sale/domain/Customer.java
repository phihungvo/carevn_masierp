package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.domain.enumeration.CustomerStatus;
import com.masi.sale.service.dto.CustomerDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Customer.
 */
@Data
@Table("customer")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("customer_code")
    private String customerCode;

    @Column("company_name")
    private String companyName;

    @Column("address")
    private String address;

    @Column("tax_code")
    private String taxCode;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("birthday")
    private LocalDate birthday;

    @Column("phone_number")
    private String phoneNumber;

    @Column("email")
    private String email;

    @Column("position")
    private String position;

    @Column("customer_owner")
    private UUID customerOwner;

    @Column("contract_signed")
    private LocalDate contractSigned;

    @Column("customer_status")
    private CustomerStatus customerStatus;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("note")
    private String note;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    @Column("contract_from")
    private LocalDate contractFrom;
    @Column("contract_to")
    private LocalDate contractTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Customer id(UUID id) {
        this.setId(id);
        return this;
    }

    public Customer customerCode(String customerCode) {
        this.setCustomerCode(customerCode);
        return this;
    }

    public Customer companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public Customer address(String address) {
        this.setAddress(address);
        return this;
    }

    public Customer taxCode(String taxCode) {
        this.setTaxCode(taxCode);
        return this;
    }

    public Customer firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public Customer lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public Customer birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public Customer phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public Customer email(String email) {
        this.setEmail(email);
        return this;
    }

    public Customer position(String position) {
        this.setPosition(position);
        return this;
    }

    public Customer customerOwner(UUID customerOwner) {
        this.setCustomerOwner(customerOwner);
        return this;
    }

    public Customer contractSigned(LocalDate contractSigned) {
        this.setContractSigned(contractSigned);
        return this;
    }

    public Customer customerStatus(CustomerStatus customerStatus) {
        this.setCustomerStatus(customerStatus);
        return this;
    }

    public Customer lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public Customer createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Customer isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Customer setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public CustomerDTO toDto() {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(this.id);
        dto.setCustomerCode(this.customerCode);
        dto.setCompanyName(this.companyName);
        dto.setAddress(this.address);
        dto.setTaxCode(this.taxCode);
        dto.setFirstName(this.firstName);
        dto.setLastName(this.lastName);
        dto.setBirthday(this.birthday);
        dto.setPhoneNumber(this.phoneNumber);
        dto.setEmail(this.email);
        dto.setPosition(this.position);
        dto.setCustomerOwner(this.customerOwner);
        dto.setContractSigned(this.contractSigned);
        dto.setNote(this.note);
        dto.setCustomerStatus(this.customerStatus);
        dto.setIsDeleted(this.isDeleted);
        dto.setLastUpdated(this.lastUpdated);
        dto.setCreatedDate(this.createdDate);
        dto.setContractFrom(this.contractFrom);
        dto.setContractTo(this.contractTo);

        return dto;
    }


}
