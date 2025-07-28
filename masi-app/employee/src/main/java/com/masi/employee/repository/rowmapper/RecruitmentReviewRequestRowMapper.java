package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.RecruitmentReviewRequest;
import com.masi.employee.domain.enumeration.PositionEmployee;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RecruitmentReviewRequest}, with proper type conversions.
 */
@Service
public class RecruitmentReviewRequestRowMapper implements BiFunction<Row, String, RecruitmentReviewRequest> {

    private final ColumnConverter converter;

    public RecruitmentReviewRequestRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RecruitmentReviewRequest} stored in the database.
     */
    @Override
    public RecruitmentReviewRequest apply(Row row, String prefix) {
        RecruitmentReviewRequest entity = new RecruitmentReviewRequest();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setPosition(converter.fromRow(row, prefix + "_position", PositionEmployee.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setRequestId(converter.fromRow(row, prefix + "_request_id", UUID.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setApprovalSignFile(converter.fromRow(row, prefix + "_approval_sign_file", String.class));
        entity.setRejectNote(converter.fromRow(row, prefix + "_reject_note", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setResult(converter.fromRow(row, prefix + "_result", Boolean.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));

        return entity;
    }
}
