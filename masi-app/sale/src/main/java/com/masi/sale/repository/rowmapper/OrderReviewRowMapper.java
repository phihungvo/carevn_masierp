package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.OrderReview;
import com.masi.sale.domain.enumeration.OrderReviewStatus;
import com.masi.sale.domain.enumeration.ReviewApproveSolution;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link OrderReview}, with proper type conversions.
 */
@Service
public class OrderReviewRowMapper implements BiFunction<Row, String, OrderReview> {

    private final ColumnConverter converter;

    public OrderReviewRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link OrderReview} stored in the database.
     */
    @Override
    public OrderReview apply(Row row, String prefix) {
        OrderReview entity = new OrderReview();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", OrderReviewStatus.class));
        entity.setApprovalStatusSignFile(converter.fromRow(row, prefix + "_approval_status_sign_file", String.class));
        entity.setApprovalStatusNote(converter.fromRow(row, prefix + "_approval_status_note", String.class));
        entity.setApprovalSolution(converter.fromRow(row, prefix + "_approval_solution", ReviewApproveSolution.class));
        entity.setAwaitingDate(converter.fromRow(row, prefix + "_awaiting_date", LocalDate.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", UUID.class));
        return entity;
    }
}
