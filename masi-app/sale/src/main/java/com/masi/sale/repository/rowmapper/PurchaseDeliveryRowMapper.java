package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.PurchaseDelivery;
import io.r2dbc.spi.Row;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PurchaseDelivery}, with proper type conversions.
 */
@Service
public class PurchaseDeliveryRowMapper implements BiFunction<Row, String, PurchaseDelivery> {

    private final ColumnConverter converter;

    public PurchaseDeliveryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link PurchaseDelivery} stored in the database.
     */
    @Override
    public PurchaseDelivery apply(Row row, String prefix) {
        PurchaseDelivery entity = new PurchaseDelivery();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setDeliveried(converter.fromRow(row, prefix + "_deliveried", Float.class));
        entity.setWaitingDelivery(converter.fromRow(row, prefix + "_waiting_delivery", Float.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setPurchaseRequestId(converter.fromRow(row, prefix + "_purchase_request_id", UUID.class));
        entity.setUpdatedDate(converter.fromRow(row, prefix + "_updated_date", ZonedDateTime.class));
        return entity;
    }
}
