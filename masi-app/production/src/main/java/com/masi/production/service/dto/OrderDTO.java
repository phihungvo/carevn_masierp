package com.masi.production.service.dto;

import io.r2dbc.postgresql.codec.Json;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class OrderDTO {
    private UUID id;
    private String code;
    private String name;
    private String fromDate;
    private String toDate;
    private ItemDTO item;
    private BigDecimal quantity;
    private Json attributes;

    private String customerCode;
    private String customerName;

    private LocalDate deliveryDateFrom;
    private LocalDate deliveryDateTo;

}
