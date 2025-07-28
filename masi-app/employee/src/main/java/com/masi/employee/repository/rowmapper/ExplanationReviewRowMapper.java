package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.ExplanationReview;
import com.masi.employee.domain.enumeration.ReviewStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ExplanationReview}, with proper type conversions.
 */
@Service
public class ExplanationReviewRowMapper implements BiFunction<Row, String, ExplanationReview> {

    private final ColumnConverter converter;

    public ExplanationReviewRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ExplanationReview} stored in the database.
     */
    @Override
    public ExplanationReview apply(Row row, String prefix) {
        ExplanationReview entity = new ExplanationReview();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ReviewStatus.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setReviewerId(converter.fromRow(row, prefix + "_reviewer_id", UUID.class));
        entity.setExplanationId(converter.fromRow(row, prefix + "_explanation_id", UUID.class));
        return entity;
    }
}
