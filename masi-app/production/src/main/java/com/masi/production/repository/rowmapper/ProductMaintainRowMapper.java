package com.masi.production.repository.rowmapper;

import com.masi.production.domain.ProductMaintain;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ProductMaintain}, with proper type conversions.
 */
@Service
public class ProductMaintainRowMapper implements BiFunction<Row, String, ProductMaintain> {

    private final ColumnConverter converter;

    public ProductMaintainRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProductMaintain} stored in the database.
     */
    @Override
    public ProductMaintain apply(Row row, String prefix) {
        ProductMaintain entity = new ProductMaintain();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setProductBatchCode(converter.fromRow(row, prefix + "_product_batch_code", String.class));
        entity.setProductBatchName(converter.fromRow(row, prefix + "_product_batch_name", String.class));
        entity.setManufactureDate(converter.fromRow(row, prefix + "_manufacture_date", LocalDate.class));
        entity.setExpiredDate(converter.fromRow(row, prefix + "_expired_date", LocalDate.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdatedAt(converter.fromRow(row, prefix + "_last_updated_at", ZonedDateTime.class));
        entity.setProductPackageId(converter.fromRow(row, prefix + "_product_package_id", UUID.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        return entity;
    }
}
