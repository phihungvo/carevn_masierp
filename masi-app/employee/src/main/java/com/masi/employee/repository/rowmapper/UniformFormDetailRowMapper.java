package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.UniformFormDetail;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UniformFormDetail}, with proper type conversions.
 */
@Service
public class UniformFormDetailRowMapper implements BiFunction<Row, String, UniformFormDetail> {

    private final ColumnConverter converter;

    public UniformFormDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UniformFormDetail} stored in the database.
     */
    @Override
    public UniformFormDetail apply(Row row, String prefix) {
        UniformFormDetail entity = new UniformFormDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setQuantityChange(converter.fromRow(row, prefix + "_returned_quantity", Integer.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setUniformId(converter.fromRow(row, prefix + "_uniform_id", UUID.class));
        entity.setUniformReleaseId(converter.fromRow(row, prefix + "_uniform_release_id", UUID.class));
        entity.setUniformOrderId(converter.fromRow(row, prefix + "_uniform_order_id", UUID.class));
        entity.setUniformReturnId(converter.fromRow(row, prefix + "_uniform_return_id", UUID.class));
        entity.setUniformOrderStockId(converter.fromRow(row, prefix + "_uniform_order_stock_id", UUID.class));
        entity.setActualPrice(converter.fromRow(row, prefix + "_actual_price", Double.class));
        entity.setUomId(converter.fromRow(row, prefix + "_uom_id", UUID.class));
        entity.setUomName(converter.fromRow(row, prefix + "_uom_name", String.class));
        return entity;
    }
}
