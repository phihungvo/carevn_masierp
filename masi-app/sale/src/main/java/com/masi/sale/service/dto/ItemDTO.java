package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.r2dbc.postgresql.codec.Json;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO implements Serializable {

    private UUID id;
    private String code;
    private String name;
    private UUID uomId;
    private Float percentProtein;

}
