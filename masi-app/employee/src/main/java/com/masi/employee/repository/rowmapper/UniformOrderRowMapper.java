package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.UniformOrder;
import com.masi.employee.domain.enumeration.UniformOrderStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UniformOrder}, with proper type
 * conversions.
 */
@Service
public class UniformOrderRowMapper implements BiFunction<Row, String, UniformOrder> {

    private final ColumnConverter converter;

    public UniformOrderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link UniformOrder} stored in the database.
     */
    @Override
    public UniformOrder apply(Row row, String prefix) {
        UniformOrder entity = new UniformOrder();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", ZonedDateTime.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", UniformOrderStatus.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setTotalBasePrice(converter.fromRow(row, prefix + "_total_base_price", Double.class));
        entity.setTotalActualPrice(converter.fromRow(row, prefix + "_total_actual_price", Double.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));
        return entity;
    }
}
