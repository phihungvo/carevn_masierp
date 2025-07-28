package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.ProcessLeaveRegimeRequest;
import com.masi.employee.domain.enumeration.ProcessLeaveRegimeRequestStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ProcessLeaveRegimeRequest}, with proper type conversions.
 */
@Service
public class ProcessLeaveRegimeRequestRowMapper implements BiFunction<Row, String, ProcessLeaveRegimeRequest> {

    private final ColumnConverter converter;

    public ProcessLeaveRegimeRequestRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProcessLeaveRegimeRequest} stored in the database.
     */
    @Override
    public ProcessLeaveRegimeRequest apply(Row row, String prefix) {
        ProcessLeaveRegimeRequest entity = new ProcessLeaveRegimeRequest();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setLeaveRegimeRequestId(converter.fromRow(row, prefix + "_leave_regime_request_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ProcessLeaveRegimeRequestStatus.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setApproverId(converter.fromRow(row, prefix + "_approver_id", UUID.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", UUID.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", UUID.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", UUID.class));
        entity.setFileId(converter.fromRow(row, prefix + "_file_id", String.class));
        entity.setFileName(converter.fromRow(row, prefix + "_file_name", String.class));
        return entity;
    }
}
