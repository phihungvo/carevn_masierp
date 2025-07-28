package com.masi.utility.repository.rowmapper;

import com.masi.utility.domain.NotificationRecipient;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link NotificationRecipient}, with proper type conversions.
 */
@Service
public class NotificationRecipientRowMapper implements BiFunction<Row, String, NotificationRecipient> {

    private final ColumnConverter converter;

    public NotificationRecipientRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link NotificationRecipient} stored in the database.
     */
    @Override
    public NotificationRecipient apply(Row row, String prefix) {
        NotificationRecipient entity = new NotificationRecipient();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setRecipientId(converter.fromRow(row, prefix + "_recipient_id", UUID.class));
        entity.setRead(converter.fromRow(row, prefix + "_read", Boolean.class));
        entity.setReadAt(converter.fromRow(row, prefix + "_read_at", ZonedDateTime.class));
        entity.setNotificationId(converter.fromRow(row, prefix + "_notification_id", UUID.class));
        return entity;
    }
}
