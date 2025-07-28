package com.masi.logistics.service.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MaterialManuFactureRequest {
    private UUID id;
    private BigDecimal quantity;
    private BigDecimal quantityUse;
    private String productionMaintainId;

}
