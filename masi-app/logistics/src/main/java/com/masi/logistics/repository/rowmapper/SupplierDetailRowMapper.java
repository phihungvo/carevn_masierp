package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.SupplierDetail;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SupplierDetail}, with proper type conversions.
 */
@Service
public class SupplierDetailRowMapper implements BiFunction<Row, String, SupplierDetail> {

    private final ColumnConverter converter;

    public SupplierDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SupplierDetail} stored in the database.
     */
    @Override
    public SupplierDetail apply(Row row, String prefix) {
        SupplierDetail entity = new SupplierDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));
        entity.setItemId(converter.fromRow(row, prefix + "_item_id", UUID.class));
        entity.setBasePrice(converter.fromRow(row, prefix + "_base_price", BigDecimal.class));
        entity.setNotes(converter.fromRow(row, prefix + "_notes", String.class));
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
