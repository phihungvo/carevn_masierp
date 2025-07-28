package com.masi.utility.repository.rowmapper;

import com.masi.utility.domain.Notification;

import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Notification}, with proper type conversions.
 */
@Service
public class NotificationRowMapper implements BiFunction<Row, String, Notification> {

    private final ColumnConverter converter;

    public NotificationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Notification} stored in the database.
     */
    @Override
    public Notification apply(Row row, String prefix) {
        Notification entity = new Notification();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setEntityName(converter.fromRow(row, prefix + "_entity_name", String.class));
        entity.setEntityId(converter.fromRow(row, prefix + "_entity_id", String.class));
        entity.setEntityType(converter.fromRow(row, prefix + "_entity_type", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));

        entity.setData(converter.fromRow(row, prefix + "_data", Json.class));
        entity.setCategory(converter.fromRow(row, prefix + "_category", String.class));
        entity.setSentBy(converter.fromRow(row, prefix + "_sent_by", String.class));
        return entity;
    }
}
