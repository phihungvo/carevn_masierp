package com.masi.production.service.dto;

import com.masi.production.domain.ProductPackage;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
@Data
public class ProductionPackagingDTO {
        private UUID id;
        private String code;
        private String name;
        private ZonedDateTime packingDate;
        private BigDecimal quantityBag;
        private UUID employeeId;
        private BigDecimal volume;
        private String note;

        private ProductPackageDTO productPackage;

}
