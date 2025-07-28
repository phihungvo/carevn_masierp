package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformOrder} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrderCreateDTO implements Serializable {

    private String name;

    private ZonedDateTime date;

    private Collection<UUID> approverIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private Integer quantity = 0;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private Double totalBasePrice = 0.0;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private Double totalActualPrice = 0.0;

    private UUID supplierId;

    @JsonIgnore
    private String company;

    private Collection<UniformFormDetailCreateDTO> details;

    public void setDetails(Collection<UniformFormDetailCreateDTO> details) {
        this.details = details;
        setQuantity();
        setTotalBasePrice();
        setTotalActualPrice();
    }

    private void setQuantity() {
        if (details != null) {
            this.quantity = details.stream().mapToInt(UniformFormDetailCreateDTO::getQuantity).sum();
        } else {
            this.quantity = 0;
        }
    }

    private void setTotalBasePrice() {
        if (details != null) {
            this.totalBasePrice = details.stream().mapToDouble(UniformFormDetailCreateDTO::getBasePrice).sum();
        } else {
            this.totalBasePrice = 0.0;
        }
    }

    private void setTotalActualPrice() {
        if (details != null) {
            this.totalActualPrice = details.stream().mapToDouble(UniformFormDetailCreateDTO::getActualPrice).sum();
        } else {
            this.totalActualPrice = 0.0;
        }
    }


    public UniformOrderDTO toDto() {
        UniformOrderDTO uniformOrderDTO = new UniformOrderDTO();
        uniformOrderDTO.setName(this.name);
        uniformOrderDTO.setDate(this.date);
        uniformOrderDTO.setQuantity(this.quantity);
        uniformOrderDTO.setCompany(this.company);
        uniformOrderDTO.setTotalBasePrice(this.totalBasePrice);
        uniformOrderDTO.setTotalActualPrice(this.totalActualPrice);
        uniformOrderDTO.setSupplierId(this.supplierId);
        return uniformOrderDTO;
    }

    // detail to list dto for save
    public List<UniformFormDetailDTO> toDetailDto(UniformOrderDTO uniformOrderDTO) {
        return details.stream().map(x -> {
            UniformFormDetailDTO uniformFormDetailDTO = new UniformFormDetailDTO();
            uniformFormDetailDTO.setQuantity(x.getQuantity());
            uniformFormDetailDTO.setUniformId(x.getUniformId());
            uniformFormDetailDTO.setUniformOrderId(uniformOrderDTO.getId());
            uniformFormDetailDTO.setBasePrice(x.getBasePrice());
            uniformFormDetailDTO.setActualPrice(x.getActualPrice());
            uniformFormDetailDTO.setUomId(x.getUomId());
            uniformFormDetailDTO.setUomName(x.getUomName());
            return uniformFormDetailDTO;

        }).toList();
    }

    public List<UniformOrderProcessDTO> toProcessDto(UniformOrderDTO uniformOrderDTO) {
        if (approverIds == null) {
            UniformOrderProcessDTO uniformOrderProcessDTO = new UniformOrderProcessDTO();
            uniformOrderProcessDTO.setUniformOrder(uniformOrderDTO);
            var process = new ArrayList<UniformOrderProcessDTO>();
            process.add(uniformOrderProcessDTO);
            return process;
        }
        return approverIds.stream().map(x -> {
            UniformOrderProcessDTO uniformOrderProcessDTO = new UniformOrderProcessDTO();
            uniformOrderProcessDTO.setUniformOrder(uniformOrderDTO);
            uniformOrderProcessDTO.setApproverId(x);
            return uniformOrderProcessDTO;
        }).toList();
    }
}
