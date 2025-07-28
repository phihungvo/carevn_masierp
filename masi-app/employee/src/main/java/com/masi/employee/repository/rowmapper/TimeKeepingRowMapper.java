package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TimeKeeping}, with proper type conversions.
 */
@Service
public class TimeKeepingRowMapper implements BiFunction<Row, String, TimeKeeping> {

    private final ColumnConverter converter;

    public TimeKeepingRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TimeKeeping} stored in the database.
     */
    @Override
    public TimeKeeping apply(Row row, String prefix) {
        TimeKeeping entity = new TimeKeeping();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", LocalDate.class));
        entity.setFirstCheckIn(converter.fromRow(row, prefix + "_first_check_in", ZonedDateTime.class));
        entity.setLastCheckIn(converter.fromRow(row, prefix + "_last_check_in", ZonedDateTime.class));
        entity.setHoursWorked(converter.fromRow(row, prefix + "_hours_worked", Float.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setIsAbnormal(converter.fromRow(row, prefix + "_is_abnormal", Boolean.class));
        entity.setIsDayOff(converter.fromRow(row, prefix + "_is_day_off", Boolean.class));
        entity.setIsWorkFromHome(converter.fromRow(row, prefix + "_is_work_from_home", Boolean.class));
        entity.setIsOverride(converter.fromRow(row, prefix + "_is_override", Boolean.class));
        entity.setLocked(converter.fromRow(row, prefix + "_locked", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdatedAt(converter.fromRow(row, prefix + "_last_updated_at", ZonedDateTime.class));
        entity.setPersonalMonthlyTimesheetId(converter.fromRow(row, prefix + "_personal_monthly_timesheet_id", UUID.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setLeaveDayType(converter.fromRow(row, prefix + "_leave_day_type", LeaveRequestDayType.class));
        entity.setViolationType(converter.fromRow(row, prefix + "_violation_type", TimeKeepingViolationType.class));
        entity.setLeaveType(converter.fromRow(row, prefix + "_leave_type", LeaveType.class));
        entity.setTimeKeepingType(converter.fromRow(row, prefix + "_type", TimeKeepingType.class));

        return entity;
    }
}
