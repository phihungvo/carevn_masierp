package com.masi.utility.repository.rowmapper;

import com.masi.utility.domain.Config;
import com.masi.utility.domain.enumeration.DataType;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Config}, with proper type conversions.
 */
@Service
public class ConfigRowMapper implements BiFunction<Row, String, Config> {

    private final ColumnConverter converter;

    public ConfigRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Config} stored in the database.
     */
    @Override
    public Config apply(Row row, String prefix) {
        Config entity = new Config();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setKey(converter.fromRow(row, prefix + "_key", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", DataType.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        return entity;
    }
}
