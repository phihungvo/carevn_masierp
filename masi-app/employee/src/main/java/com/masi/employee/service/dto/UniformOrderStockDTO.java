package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.UniformOrderStock;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link UniformOrderStock} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrderStockDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    private UUID uniformOrderId;

    private UniformOrderDTO uniformOrderDTO;

    private UUID warehouseId;

    private WarehouseDTO wareHouseDTO;

    private Set<UniformFormDetailCreateDTO> uniformFormDetailDTO;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Set<UniformFormDetailDTO> uniformFormDetail;

    private Integer totalQuantity = 0;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO createdByProfile;

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

    // detail to list dto for save
    public List<UniformFormDetailDTO> toDetailDto(UUID id) {
        return uniformFormDetailDTO.stream().map(x -> {
            UniformFormDetailDTO uniformFormDetailDTO = new UniformFormDetailDTO();
            uniformFormDetailDTO.setQuantity(x.getQuantity());
            uniformFormDetailDTO.setUniformId(x.getUniformId());
            uniformFormDetailDTO.setUniformOrderStockId(id);
            uniformFormDetailDTO.setUomId(x.getUomId());
            uniformFormDetailDTO.setActualPrice(x.getActualPrice());
            uniformFormDetailDTO.setBasePrice(x.getBasePrice());

            return uniformFormDetailDTO;
        }).toList();
    }

    // Override the setter to calculate totalQuantity
    public void setUniformFormDetailDTO(Set<UniformFormDetailCreateDTO> uniformFormDetailDTO) {
        this.uniformFormDetailDTO = uniformFormDetailDTO;
        this.totalQuantity = uniformFormDetailDTO.stream()
            .mapToInt(UniformFormDetailCreateDTO::getQuantity)
            .sum();
    }

    public UniformOrderStock toEntity() {
        UniformOrderStock uniformOrderStock = new UniformOrderStock();
        uniformOrderStock.setId(UUID.randomUUID());
        uniformOrderStock.setCode(code);
        uniformOrderStock.setUniformOrderId(uniformOrderId);
        uniformOrderStock.setTotalQuantity(totalQuantity);
        uniformOrderStock.setCreateAt(ZonedDateTime.now());
        uniformOrderStock.setCreateBy(this.createBy);
        uniformOrderStock.setUpdateAt(updateAt);
        uniformOrderStock.setUpdateBy(updateBy);
        uniformOrderStock.setDeleteAt(deleteAt);
        uniformOrderStock.setDeleteBy(deleteBy);
        uniformOrderStock.setCompany(company);
        uniformOrderStock.setWarehouseId(warehouseId);
        return uniformOrderStock;
    }

}
