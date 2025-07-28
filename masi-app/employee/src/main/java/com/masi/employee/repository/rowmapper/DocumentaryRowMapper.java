package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.Documentary;
import com.masi.employee.domain.enumeration.DocumentaryGroup;
import com.masi.employee.domain.enumeration.DocumentaryStatus;
import com.masi.employee.domain.enumeration.DocumentaryType;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Documentary}, with proper type conversions.
 */
@Service
public class DocumentaryRowMapper implements BiFunction<Row, String, Documentary> {

    private final ColumnConverter converter;

    public DocumentaryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link Documentary} stored in the database.
     */
    @Override
    public Documentary apply(Row row, String prefix) {
        Documentary entity = new Documentary();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setDocumentNumber(converter.fromRow(row, prefix + "_document_number", String.class));
        entity.setDateStart(converter.fromRow(row, prefix + "_date_start", LocalDate.class));
        entity.setGroup(converter.fromRow(row, prefix + "_documentary_group", DocumentaryGroup.class));
        entity.setType(converter.fromRow(row, prefix + "_type", DocumentaryType.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setSigner(converter.fromRow(row, prefix + "_signer", UUID.class));
        entity.setRecipient(converter.fromRow(row, prefix + "_recipient", String.class));
        entity.setArchiveLocation(converter.fromRow(row, prefix + "_archive_location", String.class));
        entity.setSenderOrReceiver(converter.fromRow(row, prefix + "_sender_or_receiver", UUID.class));
        entity.setRejectNote(converter.fromRow(row, prefix + "_reject_note", String.class));

        entity.setIdCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", DocumentaryStatus.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setAttachmentsContentFile(converter.fromRow(row, prefix + "_attachments_content_file", String.class));
        entity.setApprovalSignFile(converter.fromRow(row, prefix + "_approval_sign_file", String.class));
        entity.setAttachments(converter.fromRow(row, prefix + "_attachments", Json.class));
        return entity;
    }
}
