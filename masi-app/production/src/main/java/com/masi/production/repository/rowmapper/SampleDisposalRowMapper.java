package com.masi.production.repository.rowmapper;

import com.masi.production.domain.SampleDisposal;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SampleDisposal}, with proper type conversions.
 */
@Service
public class SampleDisposalRowMapper implements BiFunction<Row, String, SampleDisposal> {

    private final ColumnConverter converter;

    public SampleDisposalRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SampleDisposal} stored in the database.
     */
    @Override
    public SampleDisposal apply(Row row, String prefix) {
        SampleDisposal entity = new SampleDisposal();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setRequestDate(converter.fromRow(row, prefix + "_request_date", LocalDate.class));
        entity.setInvolveEmployee(converter.fromRow(row, prefix + "_involve_employee", String.class));
        entity.setPosition(converter.fromRow(row, prefix + "_position", String.class));
        entity.setDisposalNote(converter.fromRow(row, prefix + "_disposal_note", String.class));
        entity.setQuantityStt(converter.fromRow(row, prefix + "_quantity_stt", Integer.class));
        entity.setQuantitySampleName(converter.fromRow(row, prefix + "_quantity_sample_name", String.class));
        entity.setQuantitySampleNo(converter.fromRow(row, prefix + "_quantity_sample_no", String.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Float.class));
        entity.setQuantitySaveDate(converter.fromRow(row, prefix + "_quantity_save_date", LocalDate.class));
        entity.setQuantityReleaseDate(converter.fromRow(row, prefix + "_quantity_release_date", LocalDate.class));
        entity.setDisposalMethod(converter.fromRow(row, prefix + "_disposal_method", String.class));
        entity.setDisposalResult(converter.fromRow(row, prefix + "_disposal_result", String.class));
        entity.setReviewerId(converter.fromRow(row, prefix + "_reviewer_id", UUID.class));
        entity.setRequesterId(converter.fromRow(row, prefix + "_requester_id", UUID.class));
        entity.setReviewerApproved(converter.fromRow(row, prefix + "_reviewer_approved", Boolean.class));
        entity.setReviewerNote(converter.fromRow(row, prefix + "_reviewer_note", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setReviewerSignFile(converter.fromRow(row, prefix + "_reviewer_sign_file", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        return entity;
    }
}
