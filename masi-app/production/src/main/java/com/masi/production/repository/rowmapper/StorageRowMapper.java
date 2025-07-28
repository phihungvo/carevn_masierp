package com.masi.production.repository.rowmapper;

import com.masi.production.domain.Storage;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Storage}, with proper type conversions.
 */
@Service
public class StorageRowMapper implements BiFunction<Row, String, Storage> {

    private final ColumnConverter converter;

    public StorageRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Storage} stored in the database.
     */
    @Override
    public Storage apply(Row row, String prefix) {
        Storage entity = new Storage();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
