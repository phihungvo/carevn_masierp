package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.QualityIndex;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link QualityIndex}, with proper type conversions.
 */
@Service
public class QualityIndexRowMapper implements BiFunction<Row, String, QualityIndex> {

    private final ColumnConverter converter;

    public QualityIndexRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link QualityIndex} stored in the database.
     */
    @Override
    public QualityIndex apply(Row row, String prefix) {
        QualityIndex entity = new QualityIndex();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", String.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", UUID.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        return entity;
    }
}
