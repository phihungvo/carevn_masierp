package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.UniformOrderStock;
import com.masi.employee.domain.enumeration.UniformOrderStatus;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformOrder} entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrderDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private String code;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private Integer quantity;

    private Double totalBasePrice = 0.0;

    private Double totalActualPrice = 0.0;

    private UUID supplierId;

    private String supplierName;

    @NotNull(message = "must not be null")
    private ZonedDateTime date;

    @NotNull(message = "must not be null")
    private UniformOrderStatus status;

    @NotNull(message = "must not be null")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeProfileDTO createByDTO;

    private ZonedDateTime updateAt;

    private String updateBy;

    private ZonedDateTime deleteAt;

    private String deleteBy;

    private String company;

    private Integer remainQuantity = 0;

    @Transient
    private List<UniformFormDetailDTO> uniformFormDetails;

    @Transient
    private List<UniformOrderProcessDTO> uniformOrderProcesses;

    @Transient
    private List<UniformOrderStockDTO> uniformOrderStockDTOS;

}
