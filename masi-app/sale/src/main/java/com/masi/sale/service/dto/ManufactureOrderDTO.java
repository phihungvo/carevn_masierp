package com.masi.sale.service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Data
public class ManufactureOrderDTO implements Serializable {

    private UUID id;
    private String name;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String typeProtein;
    private String status;
    private UUID orderId;
    private Boolean isActive;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastUpdated;

}
