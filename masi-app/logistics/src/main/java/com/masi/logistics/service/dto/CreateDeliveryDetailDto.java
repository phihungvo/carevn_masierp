package com.masi.logistics.service.dto;

import com.masi.logistics.domain.DeliveryDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateDeliveryDetailDto {
    private UUID contractMaterialId;
    private Integer quantity;
    private UUID uomId;
    private BigDecimal price;

    public DeliveryDetail toEntity(UUID deliveryId) {
        DeliveryDetail deliveryDetail = new DeliveryDetail();
        deliveryDetail.setId(UUID.randomUUID());
        deliveryDetail.setDeliveryId(deliveryId);
        deliveryDetail.setContractMaterialId(contractMaterialId);
        deliveryDetail.setQuantity(quantity);
        deliveryDetail.setUomId(uomId);
        deliveryDetail.setPrice(price);
        return deliveryDetail;
    }

    public CreateDeliveryDetailDto(DeliveryDetail deliveryDetail) {
        if (deliveryDetail != null) {
            this.contractMaterialId = deliveryDetail.getContractMaterialId();
            this.quantity = deliveryDetail.getQuantity();
            this.uomId = deliveryDetail.getUomId();
            this.price = deliveryDetail.getPrice();
        }
    }
    public CreateDeliveryDetailDto() {
    }
}
