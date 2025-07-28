package com.masi.employee.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformFormDetail} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformFormDetailCreateDTO implements Serializable {

    private Integer quantity = 0;
    private UUID uniformId;
    private UUID uomId;
    private String uomName;
    private Double actualPrice = 0.0;
    private Double basePrice = 0.0;

    // to dto function
    public UniformFormDetailDTO toDto(UniformDTO uniformDTO) {
        UniformFormDetailDTO uniformFormDetailDTO = new UniformFormDetailDTO();

        uniformFormDetailDTO.setQuantity(this.quantity);
        uniformFormDetailDTO.setUniform(uniformDTO);
        uniformFormDetailDTO.setActualPrice(this.actualPrice);
        uniformFormDetailDTO.setBasePrice(this.basePrice);
        uniformFormDetailDTO.setUomId(this.uomId);
        uniformFormDetailDTO.setUomName(this.uomName);
        return uniformFormDetailDTO;
    }
}
