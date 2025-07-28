package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.UniformStock;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UniformStock}, with proper type conversions.
 */
@Service
public class UniformStockRowMapper implements BiFunction<Row, String, UniformStock> {

    private final ColumnConverter converter;

    public UniformStockRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UniformStock} stored in the database.
     */
    @Override
    public UniformStock apply(Row row, String prefix) {
        UniformStock entity = new UniformStock();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setStock(converter.fromRow(row, prefix + "_stock", Integer.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setUniformId(converter.fromRow(row, prefix + "_uniform_id", UUID.class));
        entity.setWarehouseId(converter.fromRow(row, prefix + "_warehouse_id", UUID.class));
        entity.setWarehouseName(converter.fromRow(row, prefix + "_warehouse_name", String.class));
        return entity;
    }
}
