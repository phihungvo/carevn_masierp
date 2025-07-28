package com.masi.employee.service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformStock} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformStockDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private Integer stock;

    private UUID warehouseId;

    private String warehouseName;

    @NotNull(message = "must not be null")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    private String createBy;

    private ZonedDateTime updateAt;

    private String updateBy;

    private ZonedDateTime deleteAt;

    private String deleteBy;

    private String company;

    private String department;

    private UniformDTO uniform;

}
