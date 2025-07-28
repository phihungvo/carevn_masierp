package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.TimeKeepingExplanation;
import com.masi.employee.domain.enumeration.ExplanationStatus;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TimeKeepingExplanation}, with proper type conversions.
 */
@Service
public class TimeKeepingExplanationRowMapper implements BiFunction<Row, String, TimeKeepingExplanation> {

    private final ColumnConverter converter;

    public TimeKeepingExplanationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TimeKeepingExplanation} stored in the database.
     */
    @Override
    public TimeKeepingExplanation apply(Row row, String prefix) {
        TimeKeepingExplanation entity = new TimeKeepingExplanation();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setExplanation(converter.fromRow(row, prefix + "_explanation", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ExplanationStatus.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", TimeKeepingViolationType.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        return entity;
    }
}
