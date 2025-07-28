package com.masi.logistics.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.logistics.domain.Warehouse;
import com.masi.logistics.domain.enumeration.ItemStatus;
import com.masi.logistics.service.dto.ItemDTO;
import com.masi.logistics.service.dto.WarehouseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncludedAccessoriesDTO {

    private UUID id;
    private String code;
    private String name;
    private BigDecimal quantity;

    private UUID itemId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ItemDTO item;

    private BigDecimal price;
    private ItemStatus status;
    private LocalDate brokenDate;
    private String addDate;

    private UUID warehouseId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WarehouseDTO warehouse;
}
