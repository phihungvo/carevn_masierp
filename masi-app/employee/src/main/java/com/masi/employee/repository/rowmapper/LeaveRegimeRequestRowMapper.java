package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.LeaveRegimeRequest;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveType;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link LeaveRegimeRequest}, with proper type
 * conversions.
 */
@Service
public class LeaveRegimeRequestRowMapper implements BiFunction<Row, String, LeaveRegimeRequest> {

    private final ColumnConverter converter;

    public LeaveRegimeRequestRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link LeaveRegimeRequest} stored in the database.
     */
    @Override
    public LeaveRegimeRequest apply(Row row, String prefix) {
        LeaveRegimeRequest entity = new LeaveRegimeRequest();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setLeaveType(converter.fromRow(row, prefix + "_leave_type", LeaveType.class));
        entity.setLastWorkDate(converter.fromRow(row, prefix + "_last_work_date", ZonedDateTime.class));
        entity.setReturnWorkDate(converter.fromRow(row, prefix + "_return_work_date", ZonedDateTime.class));
        entity.setSubstituteId(converter.fromRow(row, prefix + "_substitute_id", UUID.class));
        entity.setCompanyId(converter.fromRow(row, prefix + "_company_id", String.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setLeaveRequestDayType(
            converter.fromRow(row, prefix + "_leave_request_day_type", LeaveRequestDayType.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", LeaveRegimeRequestStatus.class));
        entity.setLeaveRequestId(converter.fromRow(row, prefix + "_leave_request_id", UUID.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", UUID.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", UUID.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", UUID.class));
        entity.setFileId(converter.fromRow(row, prefix + "_file_id", String.class));
        entity.setFileName(converter.fromRow(row, prefix + "_file_name", String.class));
        entity.setFiles(converter.fromRow(row, prefix + "_files", Json.class));
        entity.setFromTime(converter.fromRow(row, prefix + "_from_time", ZonedDateTime.class));
        entity.setToTime(converter.fromRow(row, prefix + "_to_time", ZonedDateTime.class));
        entity.setTotalDayOff(converter.fromRow(row, prefix + "_total_day_off", Float.class));
        return entity;
    }
}
