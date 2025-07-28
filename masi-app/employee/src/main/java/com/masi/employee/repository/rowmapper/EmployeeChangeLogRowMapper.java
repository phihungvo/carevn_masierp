package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.EmployeeChangeLog;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link EmployeeChangeLog}, with proper type conversions.
 */
@Service
public class EmployeeChangeLogRowMapper implements BiFunction<Row, String, EmployeeChangeLog> {

    private final ColumnConverter converter;

    public EmployeeChangeLogRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link EmployeeChangeLog} stored in the database.
     */
    @Override
    public EmployeeChangeLog apply(Row row, String prefix) {
        EmployeeChangeLog entity = new EmployeeChangeLog();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setChangeDate(converter.fromRow(row, prefix + "_change_date", ZonedDateTime.class));
        entity.setChange(converter.fromRow(row, prefix + "_change", Json.class));
        entity.setChangeBy(converter.fromRow(row, prefix + "_change_by", String.class));
        
        return entity;
    }
}
