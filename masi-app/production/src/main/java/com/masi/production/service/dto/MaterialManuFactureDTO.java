package com.masi.production.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.production.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class MaterialManuFactureDTO {
    private String id;
    private String inventoriesId;
    private String productionMaintainId;
    private BigDecimal inventoryVolume;
    private BigDecimal volumeUsed;
    private String note;
    private StatusEntity status; // Trạng thái
    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attributes;
    private ZonedDateTime mixingDate;
    private ZonedDateTime productionDate;

    private BigDecimal quantity;
    private BigDecimal quantityUse;
}
