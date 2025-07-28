package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;


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

    private UUID warehouseId;

    private String warehouseName;

    private String signatureContentType;

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

    private Set<UniformFormDetailDTO> uniformFormDetails;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer remaining;
}
