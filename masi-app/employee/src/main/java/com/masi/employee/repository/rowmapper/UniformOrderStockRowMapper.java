package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.UniformOrderStock;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UniformOrderStock}, with proper type conversions.
 */
@Service
public class UniformOrderStockRowMapper implements BiFunction<Row, String, UniformOrderStock> {

    private final ColumnConverter converter;

    public UniformOrderStockRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UniformOrderStock} stored in the database.
     */
    @Override
    public UniformOrderStock apply(Row row, String prefix) {
        UniformOrderStock entity = new UniformOrderStock();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setUniformOrderId(converter.fromRow(row, prefix + "_uniform_order_id", UUID.class));
        entity.setTotalQuantity(converter.fromRow(row, prefix + "_total_quantity", Integer.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setWarehouseId(converter.fromRow(row, prefix + "_warehouse_id", UUID.class));
        return entity;
    }
}
