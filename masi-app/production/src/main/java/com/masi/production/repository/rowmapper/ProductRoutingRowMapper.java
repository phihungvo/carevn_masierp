package com.masi.production.repository.rowmapper;

import com.masi.production.domain.ProductRouting;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ProductRouting}, with proper type conversions.
 */
@Service
public class ProductRoutingRowMapper implements BiFunction<Row, String, ProductRouting> {

    private final ColumnConverter converter;

    public ProductRoutingRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProductRouting} stored in the database.
     */
    @Override
    public ProductRouting apply(Row row, String prefix) {
        ProductRouting entity = new ProductRouting();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Float.class));
        entity.setUnit(converter.fromRow(row, prefix + "_unit", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdatedAt(converter.fromRow(row, prefix + "_last_updated_at", ZonedDateTime.class));
        entity.setFactoryId(converter.fromRow(row, prefix + "_factory_id", UUID.class));
        entity.setStorageId(converter.fromRow(row, prefix + "_storage_id", UUID.class));
        entity.setProductMaintainId(converter.fromRow(row, prefix + "_product_maintain_id", UUID.class));
        entity.setWarehouseDate(converter.fromRow(row, prefix + "_warehouse_date", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        return entity;
    }
}
