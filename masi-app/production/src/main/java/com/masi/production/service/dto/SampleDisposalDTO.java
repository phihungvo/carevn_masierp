package com.masi.production.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.SampleDisposal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.SampleDisposal} entity.
 */
@Data
@EqualsAndHashCode
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SampleDisposalDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

//    @NotNull(message = "must not be null")
    private UUID qualitySampleCheckId;

//    @NotNull(message = "must not be null")
    private LocalDate requestDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedRequestDate;

//    @NotNull(message = "must not be null")
    private String involveEmployee;

//    @NotNull(message = "must not be null")
    private String position;

//    @NotNull(message = "must not be null")
    private String disposalNote;

//    @NotNull(message = "must not be null")
    private Integer quantityStt;
//
//    @NotNull(message = "must not be null")
    private String quantitySampleName;

//    @NotNull(message = "must not be null")
    private String quantitySampleNo;

//    @NotNull(message = "must not be null")
    private Float quantity;

//    @NotNull(message = "must not be null")
    private LocalDate quantitySaveDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedQuantitySaveDate;

//    @NotNull(message = "must not be null")
    private LocalDate quantityReleaseDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedQuantityReleaseDate;

//    @NotNull(message = "must not be null")
    private String disposalMethod;

//    @NotNull(message = "must not be null")
    private String disposalResult;

//    @NotNull(message = "must not be null")
    private UUID reviewerId;

    private UUID requesterId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean reviewerApproved;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String reviewerNote;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String reviewerSignFile;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {FileAttachmentDTO.class})
    private FileAttachmentDTO reviewerSignFileDTO;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {EmployeeDTO.class})
    private EmployeeDTO reviewer;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive;

    private UUID samplingEmployeeId;

    public void applyUpdateTo(SampleDisposal sampleDisposal) {
        sampleDisposal.requestDate(this.requestDate);
        sampleDisposal.involveEmployee(this.involveEmployee);
        sampleDisposal.position(this.position);
        sampleDisposal.disposalNote(this.disposalNote);
        sampleDisposal.quantityStt(this.quantityStt);
        sampleDisposal.quantitySampleName(this.quantitySampleName);
        sampleDisposal.quantitySampleNo(this.quantitySampleNo);
        sampleDisposal.quantity(this.quantity);
        sampleDisposal.quantitySaveDate(this.quantitySaveDate);
        sampleDisposal.quantityReleaseDate(this.quantityReleaseDate);
        sampleDisposal.disposalMethod(this.disposalMethod);
        sampleDisposal.disposalResult(this.disposalResult);
        sampleDisposal.reviewerId(this.reviewerId);
        sampleDisposal.requesterId(this.requesterId);
    }

}
