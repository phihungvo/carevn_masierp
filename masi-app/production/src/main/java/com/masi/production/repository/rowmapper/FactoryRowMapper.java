package com.masi.production.repository.rowmapper;

import com.masi.production.domain.Factory;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Factory}, with proper type conversions.
 */
@Service
public class FactoryRowMapper implements BiFunction<Row, String, Factory> {

    private final ColumnConverter converter;

    public FactoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Factory} stored in the database.
     */
    @Override
    public Factory apply(Row row, String prefix) {
        Factory entity = new Factory();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
