package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.UniformRelease;
import com.masi.employee.domain.enumeration.UniformReleaseType;
import io.r2dbc.spi.Row;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UniformRelease}, with proper type
 * conversions.
 */
@Service
public class UniformReleaseRowMapper implements BiFunction<Row, String, UniformRelease> {

    private final ColumnConverter converter;

    public UniformReleaseRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link UniformRelease} stored in the database.
     */
    @Override
    public UniformRelease apply(Row row, String prefix) {
        UniformRelease entity = new UniformRelease();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", ZonedDateTime.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setSignatureContentType(converter.fromRow(row, prefix + "_signature_content_type", String.class));
        entity.setFileId(converter.fromRow(row, prefix + "_file_id", String.class));
        entity.setFileName(converter.fromRow(row, prefix + "_file_name", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", UniformReleaseType.class));
        entity.setCost(converter.fromRow(row, prefix + "_cost", Float.class));
        entity.setIsReturned(converter.fromRow(row, prefix + "_is_returned", Boolean.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setRemaining(converter.fromRow(row, prefix + "_remaining", Integer.class));
        entity.setWarehouseId(converter.fromRow(row, prefix + "_warehouse_id", UUID.class));
        return entity;
    }
}
