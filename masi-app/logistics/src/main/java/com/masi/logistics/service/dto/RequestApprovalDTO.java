package com.masi.logistics.service.dto;

import com.carevn.masi.dto.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.RequestApproval} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestApprovalDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private Integer index;

    private UUID documentId;

    private UUID employeeId;

    private EmployeeDTO employee;

    private Boolean result;

    private String approvedSign;

    private String approvedSignName;

    private String rejectNote;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted = false;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String groupRequest;

}
