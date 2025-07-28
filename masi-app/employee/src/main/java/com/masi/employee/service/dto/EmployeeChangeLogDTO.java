package com.masi.employee.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.EmployeeChangeLog} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeChangeLogDTO implements Serializable {

    private UUID id;

    private ZonedDateTime changeDate;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json change;

    private String changeBy;
    private UUID employeeId;
}
