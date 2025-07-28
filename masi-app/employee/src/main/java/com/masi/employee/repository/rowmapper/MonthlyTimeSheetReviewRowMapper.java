package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.MonthlyTimeSheetReview;
import com.masi.employee.domain.enumeration.TimesheetReviewStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MonthlyTimeSheetReview}, with proper type conversions.
 */
@Service
public class MonthlyTimeSheetReviewRowMapper implements BiFunction<Row, String, MonthlyTimeSheetReview> {

    private final ColumnConverter converter;

    public MonthlyTimeSheetReviewRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MonthlyTimeSheetReview} stored in the database.
     */
    @Override
    public MonthlyTimeSheetReview apply(Row row, String prefix) {
        MonthlyTimeSheetReview entity = new MonthlyTimeSheetReview();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", TimesheetReviewStatus.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setSignatureFile(converter.fromRow(row, prefix + "_signature_file", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        return entity;
    }
}
