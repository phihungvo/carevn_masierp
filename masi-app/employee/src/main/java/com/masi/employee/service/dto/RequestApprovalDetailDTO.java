package com.masi.employee.service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.RequestApprovalDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestApprovalDetailDTO implements Serializable {

    private UUID id;

    private Integer index = 0;

    private UUID documentId;

    private UUID employeeId;

    private Boolean isApproved;

    private String approvedSign;

    private String approvedSignName;

    private String rejectNote;

    private String entityName;

    private String company;

    private String department;

    private Boolean isDeleted;

    private String createdBy;

    private ZonedDateTime createdDate;

    private String updatedBy;

    private ZonedDateTime updatedAt;

    private String deletedBy;

    private ZonedDateTime deletedAt;

}
