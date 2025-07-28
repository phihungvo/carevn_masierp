package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.WarehouseRole;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link WarehouseRole}, with proper type conversions.
 */
@Service
public class WarehouseRoleRowMapper implements BiFunction<Row, String, WarehouseRole> {

    private final ColumnConverter converter;

    public WarehouseRoleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link WarehouseRole} stored in the database.
     */
    @Override
    public WarehouseRole apply(Row row, String prefix) {
        WarehouseRole entity = new WarehouseRole();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setRoleId(converter.fromRow(row, prefix + "_role_id", UUID.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setWarehouseTypeId(converter.fromRow(row, prefix + "_warehouse_type_id", UUID.class));
        return entity;
    }
}
