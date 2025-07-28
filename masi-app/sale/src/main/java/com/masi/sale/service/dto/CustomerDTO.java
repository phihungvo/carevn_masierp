package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.sale.domain.Customer;
import com.masi.sale.domain.enumeration.CustomerStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.masi.sale.domain.enumeration.CustomerStatus.ENABLED;

/**
 * A DTO for the {@link com.masi.sale.domain.Customer} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String customerCode;

    private String companyName;

    private String address;

    private String taxCode;

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private String phoneNumber;

    private String email;

    private String position;

    private UUID customerOwner;

    private EmployeeDTO customerOwnerDTO;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate contractSigned;

    private String note;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CustomerStatus customerStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    private LocalDate contractFrom;
    private LocalDate contractTo;


    public void applyUpdate(Customer customer) {
        // customer.setCustomerCode(this.customerCode);
        customer.setCompanyName(this.companyName);
        customer.setAddress(this.address);
        customer.setTaxCode(this.taxCode);
        customer.setFirstName(this.firstName);
        customer.setLastName(this.lastName);
        customer.setBirthday(this.birthday);
        customer.setPhoneNumber(this.phoneNumber);
        customer.setEmail(this.email);
        customer.setPosition(this.position);
        customer.setCustomerOwner(this.customerOwner);
//        customer.setContractSigned(this.contractSigned);
        customer.setNote(this.note);

        customer.setIsDeleted(this.isDeleted);
        customer.setContractFrom(this.contractFrom);
        customer.setContractTo(this.contractTo);
    }
}
