package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.Warehouse;
import com.masi.logistics.domain.enumeration.WarehouseTypePage;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Warehouse}, with proper type conversions.
 */
@Service
public class WarehouseRowMapper implements BiFunction<Row, String, Warehouse> {

    private final ColumnConverter converter;

    public WarehouseRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Warehouse} stored in the database.
     */
    @Override
    public Warehouse apply(Row row, String prefix) {
        Warehouse entity = new Warehouse();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setActive(converter.fromRow(row, prefix + "_active", Boolean.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));

        entity.setWarehouseTypeId(converter.fromRow(row, prefix + "_warehouse_type_id", UUID.class));
        entity.setWarehouseTypePage(converter.fromRow(row, prefix + "_warehouse_type_page", WarehouseTypePage.class));
        return entity;
    }
}
