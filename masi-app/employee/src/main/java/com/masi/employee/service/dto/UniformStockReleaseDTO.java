package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.UniformReleaseType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformRelease} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformStockReleaseDTO implements Serializable {

    @NotNull(message = "must not be null")
    private ZonedDateTime date;

    @NotNull(message = "must not be null")
    private UUID employeeId;

    @NotNull(message = "must not be null")
    private Integer quantity;

    private UUID warehouseId;

    private String note;

    private String fileId;

    private String fileName;

    private boolean isReturned;

    public boolean getIsReturned() {
        return isReturned;
    }

    @NotNull(message = "must not be null")
    private UniformReleaseType type;

    private Float cost = 0F;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private String code;

    private Collection<UniformFormDetailCreateDTO> details;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer remaining = 0;

    public void setDetails(Collection<UniformFormDetailCreateDTO> details) {
        this.details = details;
        setQuantity();
    }

    private void setQuantity() {
        if (details != null) {
            this.quantity = details.stream().mapToInt(UniformFormDetailCreateDTO::getQuantity).sum();
        } else {
            this.quantity = 0;
        }
    }

    public UniformReturnDTO toReturnDto() {
        return UniformReturnDTO.builder()
            .date(this.date)
            .id(UUID.randomUUID())
            .createAt(ZonedDateTime.now())
            .employeeId(this.employeeId)
            .quantity(this.quantity)
            .returnDetails(this.details.stream().map(x -> new UniformReturnDTO.ReturnDetail(x.getUniformId(), x.getQuantity())).toArray(UniformReturnDTO.ReturnDetail[]::new))
            .build();
    }

    public UniformReleaseDTO toReleaseDto() {
        var uniformReleaseDTO = new UniformReleaseDTO();
        uniformReleaseDTO.setEmployeeId(this.getEmployeeId());
        uniformReleaseDTO.setQuantity(this.getQuantity());
        uniformReleaseDTO.setCost(this.getCost());
        if (this.type == UniformReleaseType.SUPPORT) {
            uniformReleaseDTO.setCost(0f);
        }
        uniformReleaseDTO.setFileId(this.getFileId());
        uniformReleaseDTO.setFileName(this.getFileName());
        uniformReleaseDTO.setType(this.getType());
        uniformReleaseDTO.setIsReturned(this.getIsReturned());
        uniformReleaseDTO.setNote(note);
        uniformReleaseDTO.setDate(date);
        uniformReleaseDTO.setCode(code);
        uniformReleaseDTO.setRemaining(this.getRemaining());
        uniformReleaseDTO.setWarehouseId(this.getWarehouseId());
        return uniformReleaseDTO;
    }

    // detail to list dto for save
    public List<UniformFormDetailDTO> toDetailDto(UniformReleaseDTO uniformReleaseDTO) {
        return details.stream().map(x -> {
            UniformFormDetailDTO uniformFormDetailDTO = new UniformFormDetailDTO();
            uniformFormDetailDTO.setQuantity(x.getQuantity());
            uniformFormDetailDTO.setUniformId(x.getUniformId());
            uniformFormDetailDTO.setUniformReleaseId(uniformReleaseDTO.getId());
            uniformFormDetailDTO.setQuantityChange(0);
            if(isReturned) {
                uniformFormDetailDTO.setQuantityChange(x.getQuantity());
            }
            uniformFormDetailDTO.setUomId(x.getUomId());
            uniformFormDetailDTO.setActualPrice(x.getActualPrice());
            uniformFormDetailDTO.setBasePrice(x.getBasePrice());
            uniformFormDetailDTO.setUomName(x.getUomName());
            uniformFormDetailDTO.setUomId(x.getUomId());
            return uniformFormDetailDTO;
        }).toList();
    }

}
