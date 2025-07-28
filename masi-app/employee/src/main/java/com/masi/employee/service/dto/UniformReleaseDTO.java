package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.ProcessLeaveRegimeRequest;
import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformReturn;
import com.masi.employee.domain.enumeration.UniformReleaseType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.masi.employee.domain.Employee;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformRelease} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformReleaseDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private String code;

    @NotNull(message = "must not be null")
    private ZonedDateTime date;

    @NotNull(message = "must not be null")
    private UUID employeeId;

    @NotNull(message = "must not be null")
    private Integer quantity;

    private String note;

    private String fileId;

    private String fileName;

    private String signatureContentType;

    @NotNull(message = "must not be null")
    private UniformReleaseType type;

    private Float cost = 0f;

    @NotNull(message = "must not be null")
    private Boolean isReturned;

    @NotNull(message = "must not be null")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    private String createBy;

    private ZonedDateTime updateAt;

    private String updateBy;

    private ZonedDateTime deleteAt;

    private String deleteBy;

    private String company;

    @Schema(allOf = UniformFormDetail.class)
    private Set<UniformFormDetailDTO> uniformFormDetails;

    @Schema(allOf = UniformReturn.class)
    private Set<UniformReturnDTO> uniformReturn;

    @Schema(allOf = Employee.class)
    private EmployeeDTO employee;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer remaining;

    private WarehouseDTO warehouse;

    private UUID warehouseId;
}
