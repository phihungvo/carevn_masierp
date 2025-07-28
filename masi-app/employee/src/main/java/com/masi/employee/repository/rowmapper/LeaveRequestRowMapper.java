package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveRequestStatus;
import com.masi.employee.domain.enumeration.LeaveType;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link LeaveRequest}, with proper type conversions.
 */
@Service
public class LeaveRequestRowMapper implements BiFunction<Row, String, LeaveRequest> {

    private final ColumnConverter converter;

    public LeaveRequestRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link LeaveRequest} stored in the database.
     */
    @Override
    public LeaveRequest apply(Row row, String prefix) {
        LeaveRequest entity = new LeaveRequest();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", LeaveRequestStatus.class));
        entity.setFromDate(converter.fromRow(row, prefix + "_from_date", LocalDate.class));
        entity.setToDate(converter.fromRow(row, prefix + "_to_date", LocalDate.class));
        entity.setFromTime(converter.fromRow(row, prefix + "_from_time", ZonedDateTime.class));
        entity.setToTime(converter.fromRow(row, prefix + "_to_time", ZonedDateTime.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", String.class));
        entity.setLeaveRequestType(converter.fromRow(row, prefix + "_leave_request_type", LeaveType.class));
        entity.setFileAttachmentContentType(converter.fromRow(row, prefix + "_file_attachment_content_type", String.class));
        entity.setFileAttachment(converter.fromRow(row, prefix + "_file_attachment", byte[].class));
        entity.setFileAttachmentName(converter.fromRow(row, prefix + "_file_attachment_name", String.class));
        entity.setLeaveRequestDayType(converter.fromRow(row, prefix + "_leave_request_day_type", LeaveRequestDayType.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setSubstituteId(converter.fromRow(row, prefix + "_substitute_id", UUID.class));
        entity.setFileId(converter.fromRow(row, prefix + "_file_id", String.class));
        entity.setFiles(converter.fromRow(row, prefix + "_files", Json.class));
        entity.setTotalDayOff(converter.fromRow(row, prefix + "_total_day_off", Float.class));
        return entity;
    }
}
