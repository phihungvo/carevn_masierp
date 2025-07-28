package com.carevn.masi.service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.carevn.masi.domain.Company} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private String name;

    private String description;

    private UUID parentId;

    private String normalizedName;

    private String code;
    private String taxCode;
    private String website;
    private String callcenter;

    private String address;

    private String representativeName;
    private String representativePhone;
    private String representativeEmail;
    private LocalDate representativeDob;
    private String representativeIdNumber;

    private UUID imageId;

    private Boolean isDeleted = false;
    private Boolean isActivated;

}
