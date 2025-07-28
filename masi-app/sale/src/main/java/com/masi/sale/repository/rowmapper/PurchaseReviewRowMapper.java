package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.PurchaseReview;
import com.masi.sale.domain.enumeration.PurchaseReviewStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PurchaseReview}, with proper type conversions.
 */
@Service
public class PurchaseReviewRowMapper implements BiFunction<Row, String, PurchaseReview> {

    private final ColumnConverter converter;

    public PurchaseReviewRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PurchaseReview} stored in the database.
     */
    @Override
    public PurchaseReview apply(Row row, String prefix) {
        PurchaseReview entity = new PurchaseReview();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", PurchaseReviewStatus.class));
        entity.setApprovalStatusSignFile(converter.fromRow(row, prefix + "_approval_status_sign_file", String.class));
        entity.setApprovalStatusNote(converter.fromRow(row, prefix + "_approval_status_note", String.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setPurchaseRequestId(converter.fromRow(row, prefix + "_purchase_request_id", UUID.class));
        return entity;
    }
}
