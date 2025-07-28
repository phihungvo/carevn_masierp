package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.PaymentDetail;
import io.r2dbc.spi.Row;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PaymentDetail}, with proper type conversions.
 */
@Service
public class PaymentDetailRowMapper implements BiFunction<Row, String, PaymentDetail> {

    private final ColumnConverter converter;

    public PaymentDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PaymentDetail} stored in the database.
     */
    @Override
    public PaymentDetail apply(Row row, String prefix) {
        PaymentDetail entity = new PaymentDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setPaymentRequestId(converter.fromRow(row, prefix + "_payment_request_id", UUID.class));
        entity.setInvoiceId(converter.fromRow(row, prefix + "_invoice_id", UUID.class));

        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        return entity;
    }
}
