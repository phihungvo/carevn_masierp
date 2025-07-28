package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.UomGroup;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UomGroup}, with proper type conversions.
 */
@Service
public class UomGroupRowMapper implements BiFunction<Row, String, UomGroup> {

    private final ColumnConverter converter;

    public UomGroupRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UomGroup} stored in the database.
     */
    @Override
    public UomGroup apply(Row row, String prefix) {
        UomGroup entity = new UomGroup();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setBaseUomId(converter.fromRow(row, prefix + "_base_uom_id", UUID.class));
        return entity;
    }
}
