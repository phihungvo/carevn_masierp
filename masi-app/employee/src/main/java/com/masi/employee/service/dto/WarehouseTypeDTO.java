package com.masi.employee.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarehouseTypeDTO implements Serializable {

    private UUID id;

    private String code;

    private String name;

    private String address;

    private Boolean active;

    @NotNull(message = "must not be null")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    private String createBy;

    private ZonedDateTime updateAt;

    private String updateBy;

    private ZonedDateTime deleteAt;

    private String deleteBy;

    private String company;

    private WarehouseTypeDTO warehouseType;

}
