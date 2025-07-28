package com.masi.employee.service.dto;

import com.masi.employee.domain.Uniform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformFormDetail} entity.
 */
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class UniformFormDetailRawDTO implements Serializable {

    private UUID id;

    private Integer quantity;

    private Double actualPrice;

    private UUID uomId;

    private String uomName;

    @NotNull(message = "must not be null")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    private String createBy;

    private ZonedDateTime updateAt;

    private String updateBy;

    private ZonedDateTime deleteAt;

    private String deleteBy;

    private String company;

    @Schema(allOf = {Uniform.class})
    private UniformDTO uniform;

    private UUID uniformId;

    private Integer quantityChange;


}
