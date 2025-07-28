package com.masi.employee.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.employee.domain.enumeration.GroupCs;
import com.masi.employee.domain.enumeration.StatusEntity;
import com.masi.employee.domain.enumeration.TypeCS;
import com.masi.employee.domain.enumeration.TypePageCS;
import io.r2dbc.postgresql.codec.Json;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.CallCenter} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CallCenterDTO implements Serializable {

    private UUID id;

    private String code;

    private ZonedDateTime receptionDate;

    private GroupCs groupCS;

    private String phoneOfCaller;

    private String phoneOfName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusEntity status;

    private TypeCS typeCS;

    private UUID customerId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CustomerDTO customer;

    private UUID employeeCreatedId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeProfileDTO employeeCreated;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

    private TypePageCS typePageCs;

    private ZonedDateTime employeeAssignDate;
    private ZonedDateTime employeeCloseDate;
    private ZonedDateTime confirmDate;

    @Lob
    private String sourceCs;

    @Lob
    private String problemContent;

    @Lob
    private String resolutionContent;

    @Lob
    private String responseContent;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attachment;

    private UUID employeeAssignId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeProfileDTO employeeAssign;

    private UUID employeeCloseId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeProfileDTO employeeClose;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;
    private EmployeeProfileDTO createdByEmployee;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;



}
