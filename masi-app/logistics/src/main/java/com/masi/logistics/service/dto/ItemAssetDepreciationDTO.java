package com.masi.logistics.service.dto;

import com.carevn.masi.dto.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.logistics.domain.ItemAssetDepreciationDetail;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.domain.enumeration.TypePageDepreciation;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.ItemAssetDepreciation} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemAssetDepreciationDTO implements Serializable {

    private UUID id;

    private String code;

    private String attribute;

    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusEntity status;

    private LocalDate depreciationDate;

    private LocalDate accountingDate;

    private UUID employeeId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO employee;

    private String description;

    private TypePageDepreciation typePageDepreciation;

    private List<ItemAssetDepreciationDetailDTO> itemAssetDepreciationDetails;

    private Collection<RequestApprovalDTO> requestApprovals;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

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
