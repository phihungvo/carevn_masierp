package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.PurchaseRequest;
import com.masi.sale.domain.enumeration.PurchaseRequestStatus;
import com.masi.sale.domain.enumeration.Unit;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PurchaseRequest}, with proper type conversions.
 */
@Service
public class PurchaseRequestRowMapper implements BiFunction<Row, String, PurchaseRequest> {

    private final ColumnConverter converter;

    public PurchaseRequestRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PurchaseRequest} stored in the database.
     */
    @Override
    public PurchaseRequest apply(Row row, String prefix) {
        PurchaseRequest entity = new PurchaseRequest();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setRequestStatus(converter.fromRow(row, prefix + "_request_status", PurchaseRequestStatus.class));
        entity.setProductName(converter.fromRow(row, prefix + "_product_name", String.class));
        entity.setUnit(converter.fromRow(row, prefix + "_unit", Unit.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Float.class));
        entity.setUnitPrice(converter.fromRow(row, prefix + "_unit_price", Float.class));
        entity.setTotalPrice(converter.fromRow(row, prefix + "_total_price", Float.class));
        entity.setSupplier(converter.fromRow(row, prefix + "_supplier", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setCreateDate(converter.fromRow(row, prefix + "_create_date", ZonedDateTime.class));
        entity.setUpdatedDate(converter.fromRow(row, prefix + "_updated_date", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
