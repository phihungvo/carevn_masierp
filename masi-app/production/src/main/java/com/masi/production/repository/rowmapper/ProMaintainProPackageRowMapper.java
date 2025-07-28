package com.masi.production.repository.rowmapper;

import com.masi.production.domain.ProMaintainProPackage;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Converter between {@link Row} to {@link ProMaintainProPackage}, with proper type conversions.
 */
@Service
public class ProMaintainProPackageRowMapper implements BiFunction<Row, String, ProMaintainProPackage> {

    private final ColumnConverter converter;

    public ProMaintainProPackageRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProMaintainProPackage} stored in the database.
     */
    @Override
    public ProMaintainProPackage apply(Row row, String prefix) {
        ProMaintainProPackage entity = new ProMaintainProPackage();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setProductMaintainId(converter.fromRow(row, prefix + "_product_maintain_id", UUID.class));
        entity.setProductPackageId(converter.fromRow(row, prefix + "_product_package_id", UUID.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        return entity;
    }
}
