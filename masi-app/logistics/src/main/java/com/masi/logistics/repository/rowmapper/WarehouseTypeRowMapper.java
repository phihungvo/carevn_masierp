package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.WarehouseType;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link WarehouseType}, with proper type conversions.
 */
@Service
public class WarehouseTypeRowMapper implements BiFunction<Row, String, WarehouseType> {

    private final ColumnConverter converter;

    public WarehouseTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link WarehouseType} stored in the database.
     */
    @Override
    public WarehouseType apply(Row row, String prefix) {
        WarehouseType entity = new WarehouseType();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setUseManufacture(converter.fromRow(row, prefix + "_use_manufacture", Boolean.class));
        entity.setActive(converter.fromRow(row, prefix + "_active", Boolean.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        return entity;
    }
}
