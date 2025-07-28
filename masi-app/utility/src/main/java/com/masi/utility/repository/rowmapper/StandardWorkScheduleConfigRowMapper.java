package com.masi.utility.repository.rowmapper;

import com.masi.utility.domain.StandardWorkScheduleConfig;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link StandardWorkScheduleConfig}, with proper type conversions.
 */
@Service
public class StandardWorkScheduleConfigRowMapper implements BiFunction<Row, String, StandardWorkScheduleConfig> {

    private final ColumnConverter converter;

    public StandardWorkScheduleConfigRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link StandardWorkScheduleConfig} stored in the database.
     */
    @Override
    public StandardWorkScheduleConfig apply(Row row, String prefix) {
        StandardWorkScheduleConfig entity = new StandardWorkScheduleConfig();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDayOfWeek(converter.fromRow(row, prefix + "_day_of_week", Integer.class));
        entity.setNumberOfShifts(converter.fromRow(row, prefix + "_number_of_shifts", Integer.class));
        entity.setWorkHours(converter.fromRow(row, prefix + "_work_hours", Integer.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartmentType(converter.fromRow(row, prefix + "_department_type", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        return entity;
    }
}
