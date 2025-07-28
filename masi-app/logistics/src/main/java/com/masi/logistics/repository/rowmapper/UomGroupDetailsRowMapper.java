package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.UomGroupDetails;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UomGroupDetails}, with proper type conversions.
 */
@Service
public class UomGroupDetailsRowMapper implements BiFunction<Row, String, UomGroupDetails> {

    private final ColumnConverter converter;

    public UomGroupDetailsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UomGroupDetails} stored in the database.
     */
    @Override
    public UomGroupDetails apply(Row row, String prefix) {
        UomGroupDetails entity = new UomGroupDetails();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setBaseQty(converter.fromRow(row, prefix + "_base_qty", Integer.class));
        entity.setAltQty(converter.fromRow(row, prefix + "_alt_qty", Integer.class));
        entity.setActive(converter.fromRow(row, prefix + "_active", Boolean.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setBaseUomId(converter.fromRow(row, prefix + "_base_uom_id", UUID.class));
        entity.setAltUomId(converter.fromRow(row, prefix + "_alt_uom_id", UUID.class));
        entity.setUomGroupId(converter.fromRow(row, prefix + "_uom_group_id", UUID.class));
        return entity;
    }
}
