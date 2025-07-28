package com.masi.utility.repository.rowmapper;

import com.masi.utility.domain.FileAttachment;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FileAttachment}, with proper type conversions.
 */
@Service
public class FileAttachmentRowMapper implements BiFunction<Row, String, FileAttachment> {

    private final ColumnConverter converter;

    public FileAttachmentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FileAttachment} stored in the database.
     */
    @Override
    public FileAttachment apply(Row row, String prefix) {
        FileAttachment entity = new FileAttachment();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setPath(converter.fromRow(row, prefix + "_path", String.class));
        entity.setFileSize(converter.fromRow(row, prefix + "_file_size", Long.class));
        entity.setMimeType(converter.fromRow(row, prefix + "_mime_type", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        return entity;
    }
}
