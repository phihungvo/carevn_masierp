package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.Material;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Material}, with proper type conversions.
 */
@Service
public class MaterialRowMapper implements BiFunction<Row, String, Material> {

    private final ColumnConverter converter;

    public MaterialRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Material} stored in the database.
     */
    @Override
    public Material apply(Row row, String prefix) {
        Material entity = new Material();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setNameEn(converter.fromRow(row, prefix + "_name_en", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
