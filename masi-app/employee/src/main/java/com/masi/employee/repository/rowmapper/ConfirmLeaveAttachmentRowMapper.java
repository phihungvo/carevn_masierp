package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.ConfirmLeaveAttachment;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ConfirmLeaveAttachment}, with proper type conversions.
 */
@Service
public class ConfirmLeaveAttachmentRowMapper implements BiFunction<Row, String, ConfirmLeaveAttachment> {

    private final ColumnConverter converter;

    public ConfirmLeaveAttachmentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ConfirmLeaveAttachment} stored in the database.
     */
    @Override
    public ConfirmLeaveAttachment apply(Row row, String prefix) {
        ConfirmLeaveAttachment entity = new ConfirmLeaveAttachment();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setFileId(converter.fromRow(row, prefix + "_file_id", UUID.class));
        entity.setConfirmLeaveId(converter.fromRow(row, prefix + "_confirm_leave_id", UUID.class));
        return entity;
    }
}
