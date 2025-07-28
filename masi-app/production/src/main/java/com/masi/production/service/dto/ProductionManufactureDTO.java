package com.masi.production.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.production.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class ProductionManufactureDTO {
    private UUID id;
    private String code;
    private String name;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private UUID employeeId;
    private String note;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attributes;
}

