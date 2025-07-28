package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.ConfirmLeave;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ConfirmLeave}, with proper type conversions.
 */
@Service
public class ConfirmLeaveRowMapper implements BiFunction<Row, String, ConfirmLeave> {

    private final ColumnConverter converter;

    public ConfirmLeaveRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ConfirmLeave} stored in the database.
     */
    @Override
    public ConfirmLeave apply(Row row, String prefix) {
        ConfirmLeave entity = new ConfirmLeave();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setSubmissionDate(converter.fromRow(row, prefix + "_submission_date", LocalDate.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", String.class));
        entity.setRecruitmentSolution(converter.fromRow(row, prefix + "_recruitment_solution", String.class));
        entity.setHrSolution(converter.fromRow(row, prefix + "_hr_solution", String.class));
        entity.setLeaveDate(converter.fromRow(row, prefix + "_leave_date", LocalDate.class));
        return entity;
    }
}
