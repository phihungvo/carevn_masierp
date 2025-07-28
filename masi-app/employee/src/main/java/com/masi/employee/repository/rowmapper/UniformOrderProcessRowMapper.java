package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.UniformOrderProcess;
import com.masi.employee.domain.enumeration.UniformOrderProcessStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UniformOrderProcess}, with proper type conversions.
 */
@Service
public class UniformOrderProcessRowMapper implements BiFunction<Row, String, UniformOrderProcess> {

    private final ColumnConverter converter;

    public UniformOrderProcessRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UniformOrderProcess} stored in the database.
     */
    @Override
    public UniformOrderProcess apply(Row row, String prefix) {
        UniformOrderProcess entity = new UniformOrderProcess();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setApproverId(converter.fromRow(row, prefix + "_approver_id", UUID.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", UniformOrderProcessStatus.class));
        entity.setFileId(converter.fromRow(row, prefix + "_file_id", String.class));
        entity.setFileName(converter.fromRow(row, prefix + "_file_name", String.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setUniformOrderId(converter.fromRow(row, prefix + "_uniform_order_id", UUID.class));
        return entity;
    }
}
