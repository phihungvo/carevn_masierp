package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.EmployeeShiftDetail;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link EmployeeShiftDetail}, with proper type conversions.
 */
@Service
public class EmployeeShiftDetailRowMapper implements BiFunction<Row, String, EmployeeShiftDetail> {

    private final ColumnConverter converter;

    public EmployeeShiftDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link EmployeeShiftDetail} stored in the database.
     */
    @Override
    public EmployeeShiftDetail apply(Row row, String prefix) {
        EmployeeShiftDetail entity = new EmployeeShiftDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", LocalDate.class));
        entity.setTimeKeepingId(converter.fromRow(row, prefix + "_time_keeping_id", UUID.class));
        entity.setCheckInTime(converter.fromRow(row, prefix + "_check_in_time", ZonedDateTime.class));
        entity.setCheckOutTime(converter.fromRow(row, prefix + "_check_out_time", ZonedDateTime.class));
        entity.setCompletionPercent(converter.fromRow(row, prefix + "_completion_percent", Float.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setShiftId(converter.fromRow(row, prefix + "_shift_id", UUID.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));

        entity.setIsShiftOff(converter.fromRow(row, prefix + "_is_shift_off", Boolean.class));
        entity.setLeaveDayType(converter.fromRow(row, prefix + "_leave_day_type", LeaveType.class));
        entity.setIsWfh(converter.fromRow(row, prefix + "_is_wfh", Boolean.class));
        entity.setViolationId(converter.fromRow(row, prefix + "_violation_id", UUID.class));
        entity.setViolationType(converter.fromRow(row, prefix + "_violation_type", TimeKeepingViolationType.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setIsPaidShift(converter.fromRow(row, prefix + "_is_paid_shift", Boolean.class));

        return entity;
    }
}
