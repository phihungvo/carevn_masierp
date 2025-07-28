package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.LeaveDay;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveType;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link LeaveDay}, with proper type conversions.
 */
@Service
public class LeaveDayRowMapper implements BiFunction<Row, String, LeaveDay> {

    private final ColumnConverter converter;

    public LeaveDayRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link LeaveDay} stored in the database.
     */
    @Override
    public LeaveDay apply(Row row, String prefix) {
        LeaveDay entity = new LeaveDay();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setLeaveRequestId(converter.fromRow(row, prefix + "_leave_request_id", UUID.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", LocalDate.class));
        entity.setLeaveRequestDayType(converter.fromRow(row, prefix + "_leave_request_day_type", LeaveRequestDayType.class));
        entity.setLeaveType(converter.fromRow(row, prefix + "_leave_type", LeaveType.class));
        entity.setIsLocked(converter.fromRow(row, prefix + "_is_locked", Boolean.class));
        return entity;
    }
}
