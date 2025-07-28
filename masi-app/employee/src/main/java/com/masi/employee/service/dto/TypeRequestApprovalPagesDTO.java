package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.TypeRequestApproval;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.TypeRequestApprovalPages} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeRequestApprovalPagesDTO implements Serializable {


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String pageName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TypeRequestApproval type;

    private Integer numberOfReviewers;

    private Boolean isDepartment;

    // Xem coi có cần chỉ định người duyệt không
    private Boolean isNominate;

    /*
            Khi nào TypeRequestApproval == Sequentially hoặc isNominate == true  thì mới vô đây
        Json
        {
           "employees": ['uuid1', 'uuid2', 'uuid3'],
        }
     */

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Json note;
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

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

    private ZonedDateTime deletedAt;

}
