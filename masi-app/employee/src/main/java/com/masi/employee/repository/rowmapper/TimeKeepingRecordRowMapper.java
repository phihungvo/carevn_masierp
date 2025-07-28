package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.TimeKeepingRecord;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TimeKeepingRecord}, with proper type conversions.
 */
@Service
public class TimeKeepingRecordRowMapper implements BiFunction<Row, String, TimeKeepingRecord> {

    private final ColumnConverter converter;

    public TimeKeepingRecordRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TimeKeepingRecord} stored in the database.
     */
    @Override
    public TimeKeepingRecord apply(Row row, String prefix) {
        TimeKeepingRecord entity = new TimeKeepingRecord();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCheckIn(converter.fromRow(row, prefix + "_check_in", ZonedDateTime.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        return entity;
    }
}
