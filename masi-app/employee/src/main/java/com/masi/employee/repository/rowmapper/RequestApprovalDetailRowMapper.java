package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.RequestApprovalDetail;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RequestApprovalDetail}, with proper type conversions.
 */
@Service
public class RequestApprovalDetailRowMapper implements BiFunction<Row, String, RequestApprovalDetail> {

    private final ColumnConverter converter;

    public RequestApprovalDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RequestApprovalDetail} stored in the database.
     */
    @Override
    public RequestApprovalDetail apply(Row row, String prefix) {
        RequestApprovalDetail entity = new RequestApprovalDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setIndex(converter.fromRow(row, prefix + "_index", Integer.class));
        entity.setDocumentId(converter.fromRow(row, prefix + "_document_id", UUID.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setIsApproved(converter.fromRow(row, prefix + "_is_approved", Boolean.class));
        entity.setApprovedSign(converter.fromRow(row, prefix + "_approved_sign", String.class));
        entity.setApprovedSignName(converter.fromRow(row, prefix + "_approved_sign_name", String.class));
        entity.setRejectNote(converter.fromRow(row, prefix + "_reject_note", String.class));
        entity.setEntityName(converter.fromRow(row, prefix + "_entity_name", String.class));
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
