package com.masi.production.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.r2dbc.postgresql.codec.Json;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class ProductionQualityDTO {
        private UUID id;
        private String code;
        private String name;
        private QualityCheckSampleDTO qualityCheckSample;
        @JsonSerialize(using = PgJsonObjectSerializer.class)
        @JsonDeserialize(using = PgJsonObjectDeserializer.class)
        private Json attributes;
}
