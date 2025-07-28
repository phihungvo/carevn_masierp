package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.LeaveRequestReview;
import com.masi.employee.domain.enumeration.ReviewStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link LeaveRequestReview}, with proper type conversions.
 */
@Service
public class LeaveRequestReviewRowMapper implements BiFunction<Row, String, LeaveRequestReview> {

    private final ColumnConverter converter;

    public LeaveRequestReviewRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link LeaveRequestReview} stored in the database.
     */
    @Override
    public LeaveRequestReview apply(Row row, String prefix) {
        LeaveRequestReview entity = new LeaveRequestReview();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ReviewStatus.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setReviewerId(converter.fromRow(row, prefix + "_reviewer_id", UUID.class));
        entity.setLeaveRequestId(converter.fromRow(row, prefix + "_leave_request_id", UUID.class));
        entity.setFileId(converter.fromRow(row, prefix + "_file_id", String.class));
        entity.setFileName(converter.fromRow(row, prefix + "_file_name", String.class));
        return entity;
    }
}
