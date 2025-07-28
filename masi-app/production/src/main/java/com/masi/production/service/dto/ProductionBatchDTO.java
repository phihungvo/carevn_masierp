package com.masi.production.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class ProductionBatchDTO {
        private UUID id;
        private String code;
        private String name;
        private ZonedDateTime productionDate;
        private ZonedDateTime expiryDate;

        private ProductMaintainDTO productMaintain;
}
