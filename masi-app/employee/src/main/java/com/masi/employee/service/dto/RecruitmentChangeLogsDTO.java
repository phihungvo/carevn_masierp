package com.masi.employee.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.r2dbc.postgresql.codec.Json;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class RecruitmentChangeLogsDTO {
    private UUID id;

    private ZonedDateTime changeDate;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json change;

    private String changeBy;
    private UUID recruitmentRequestId;
}
