package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrderStockDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    private UUID uniformOrderId;

    private UUID warehouseId;

    private WarehouseDTO wareHouseDTO;

    //private Set<UniformFormDetailCreateDTO> uniformFormDetailDTO;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Set<UniformFormDetailDTO> uniformFormDetail;

    private Integer totalQuantity = 0;

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

}
