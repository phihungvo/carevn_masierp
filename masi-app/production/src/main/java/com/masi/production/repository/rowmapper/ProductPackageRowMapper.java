package com.masi.production.repository.rowmapper;

import com.masi.production.domain.ProductPackage;
import com.masi.production.domain.enumeration.ProductPackageStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ProductPackage}, with proper type conversions.
 */
@Service
public class ProductPackageRowMapper implements BiFunction<Row, String, ProductPackage> {

    private final ColumnConverter converter;

    public ProductPackageRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProductPackage} stored in the database.
     */
    @Override
    public ProductPackage apply(Row row, String prefix) {
        ProductPackage entity = new ProductPackage();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setPackageCode(converter.fromRow(row, prefix + "_package_code", String.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Float.class));
        entity.setUnit(converter.fromRow(row, prefix + "_unit", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdatedAt(converter.fromRow(row, prefix + "_last_updated_at", ZonedDateTime.class));
        entity.setWorkOrderId(converter.fromRow(row, prefix + "_work_order_id", UUID.class));
        entity.setManufactureOrderId(converter.fromRow(row, prefix + "_manufacture_order_id", UUID.class));
        entity.setMaterialId(converter.fromRow(row, prefix + "_material_id", UUID.class));
        entity.setIsSew(converter.fromRow(row, prefix + "_is_sew", Boolean.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ProductPackageStatus.class));
        entity.setWeight(converter.fromRow(row, prefix + "_weight", Float.class));
        entity.setPackageBy(converter.fromRow(row, prefix + "_package_by", UUID.class));
        entity.setPackageAt(converter.fromRow(row, prefix + "_package_at", ZonedDateTime.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        return entity;
    }
}
