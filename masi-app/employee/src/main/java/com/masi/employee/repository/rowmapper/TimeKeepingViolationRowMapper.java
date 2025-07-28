package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.TimeKeepingViolation;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import io.r2dbc.spi.Row;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TimeKeepingViolation}, with proper type conversions.
 */
@Service
public class TimeKeepingViolationRowMapper implements BiFunction<Row, String, TimeKeepingViolation> {

    private final ColumnConverter converter;

    public TimeKeepingViolationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link TimeKeepingViolation} stored in the database.
     */
    @Override
    public TimeKeepingViolation apply(Row row, String prefix) {
        TimeKeepingViolation entity = new TimeKeepingViolation();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setType(converter.fromRow(row, prefix + "_type", TimeKeepingViolationType.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdatedAt(converter.fromRow(row, prefix + "_last_updated_at", ZonedDateTime.class));
        entity.setTimeKeepingId(converter.fromRow(row, prefix + "_time_keeping_id", UUID.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setExplanationId(converter.fromRow(row, prefix + "_explanation_id", UUID.class));
        return entity;
    }
}
