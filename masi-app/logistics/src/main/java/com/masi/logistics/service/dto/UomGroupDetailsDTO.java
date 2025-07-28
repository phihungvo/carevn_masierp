package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.UomGroupDetails} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UomGroupDetailsDTO implements Serializable {


    private UUID id;

    private String name;

    private Integer baseQty;

    private Integer altQty;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean active;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updateAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deleteAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deleteBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    private UUID baseUomId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UomDTO baseUom;

    private UUID altUomId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UomDTO altUom;

    private UUID uomGroupId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UomGroupDTO uomGroup;
}
