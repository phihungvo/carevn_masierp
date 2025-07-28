package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.UomGroup} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UomGroupDTO implements Serializable {


    private UUID id;

    private String name;

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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UomDTO baseUom;

    private UUID baseUomId;

    private List<UomGroupDetailsDTO> uomGroupDetailsDTOs;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<UomDTO> listUomOfGroup;
}
