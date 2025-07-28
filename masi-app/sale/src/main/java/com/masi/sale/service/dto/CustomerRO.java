package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.sale.domain.enumeration.CustomerStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class CustomerRO {
    private String search;
    private LocalDate contractFrom;
    private LocalDate contractTo;
    private List<UUID> listEmployeeOwner;

    private LocalDate birthdayFrom;
    private LocalDate birthdayTo;
    private List<UUID> customerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CustomerStatus customerStatus;

}
