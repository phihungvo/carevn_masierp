package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.RequestApproval;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Converter between {@link Row} to {@link RequestApproval}, with proper type conversions.
 */
@Service
public class RequestApprovalRowMapper implements BiFunction<Row, String, RequestApproval> {

    private final ColumnConverter converter;

    public RequestApprovalRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RequestApproval} stored in the database.
     */
    @Override
    public RequestApproval apply(Row row, String prefix) {
        RequestApproval entity = new RequestApproval();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setIndex(converter.fromRow(row, prefix + "_index", Integer.class));
        entity.setDocumentId(converter.fromRow(row, prefix + "_document_id", UUID.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setResult(converter.fromRow(row, prefix + "_result", Boolean.class));
        entity.setApprovedSign(converter.fromRow(row, prefix + "_approved_sign", String.class));
        entity.setApprovedSignName(converter.fromRow(row, prefix + "_approved_sign_name", String.class));
        entity.setRejectNote(converter.fromRow(row, prefix + "_reject_note", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        return entity;
    }
}
