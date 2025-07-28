package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.DayOff;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DayOff}, with proper type conversions.
 */
@Service
public class DayOffRowMapper implements BiFunction<Row, String, DayOff> {

    private final ColumnConverter converter;

    public DayOffRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DayOff} stored in the database.
     */
    @Override
    public DayOff apply(Row row, String prefix) {
        DayOff entity = new DayOff();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setNumberDaysOff(converter.fromRow(row, prefix + "_number_days_off", Float.class));
        entity.setYear(converter.fromRow(row, prefix + "_year", Integer.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setAnnualLeave(converter.fromRow(row, prefix + "_annual_leave", UUID.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setUseDaysOff(converter.fromRow(row, prefix + "_use_days_off", Float.class));
        entity.setNowDaysOff(converter.fromRow(row, prefix + "_now_days_off", Float.class));
        return entity;
    }
}
