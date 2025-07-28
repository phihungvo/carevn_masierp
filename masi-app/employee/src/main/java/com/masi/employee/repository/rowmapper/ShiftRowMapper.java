package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.Shift;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Shift}, with proper type conversions.
 */
@Service
public class ShiftRowMapper implements BiFunction<Row, String, Shift> {

    private final ColumnConverter converter;

    public ShiftRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Shift} stored in the database.
     */
    @Override
    public Shift apply(Row row, String prefix) {
        Shift entity = new Shift();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setIdStandardWorkScheduleConfig(converter.fromRow(row, prefix + "_id_standard_work_schedule_config", UUID.class));
        entity.setShiftName(converter.fromRow(row, prefix + "_shift_name", String.class));
        entity.setDurationHours(converter.fromRow(row, prefix + "_duration_hours", Float.class));
        entity.setHourStartTime(converter.fromRow(row, prefix + "_hour_start_time", Integer.class));
        entity.setMinuteStartTime(converter.fromRow(row, prefix + "_minute_start_time", Integer.class));
        entity.setSecondStartTime(converter.fromRow(row, prefix + "_second_start_time", Integer.class));
        entity.setHourEndTime(converter.fromRow(row, prefix + "_hour_end_time", Integer.class));
        entity.setMinuteEndTime(converter.fromRow(row, prefix + "_minute_end_time", Integer.class));
        entity.setSecondEndTime(converter.fromRow(row, prefix + "_second_end_time", Integer.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        return entity;
    }
}
