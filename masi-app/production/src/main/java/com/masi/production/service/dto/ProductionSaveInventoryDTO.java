package com.masi.production.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class ProductionSaveInventoryDTO {
        private UUID id;
        private String code;
        private String name;
        private UUID warehouseId;
        private BigDecimal volume;
}
