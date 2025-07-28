package com.masi.sale.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.masi.sale.domain.QualityIndex;

/**
 * A DTO for the {@link com.masi.sale.domain.QualityIndex} entity.
 */
@Data
public class CreateQuantityIndexDto implements Serializable {


    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String value;

    public QualityIndex toEntity(UUID orderId){
        QualityIndex qualityIndex = new QualityIndex();
        qualityIndex.setId(UUID.randomUUID());
        qualityIndex.setName(this.name);
        qualityIndex.setValue(this.value);
        qualityIndex.setOrderId(orderId);
        qualityIndex.setCreatedAt(ZonedDateTime.now());
        return qualityIndex;
    }
}
