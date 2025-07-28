package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.EmployeeIdSequence;
import com.masi.employee.domain.enumeration.Gender;
import io.r2dbc.spi.Row;

import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link EmployeeIdSequence}, with proper type conversions.
 */
@Service
public class EmployeeIdSequenceRowMapper implements BiFunction<Row, String, EmployeeIdSequence> {

    private final ColumnConverter converter;

    public EmployeeIdSequenceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link EmployeeIdSequence} stored in the database.
     */
    @Override
    public EmployeeIdSequence apply(Row row, String prefix) {
        EmployeeIdSequence entity = new EmployeeIdSequence();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCurrentSequence(converter.fromRow(row, prefix + "_current_sequence", Integer.class));
        entity.setGender(converter.fromRow(row, prefix + "_gender", Gender.class));
        entity.setWorkspaceId(converter.fromRow(row, prefix + "_workspace_id", String.class));
        entity.setJavaFormat(converter.fromRow(row, prefix + "_java_format", String.class));
        return entity;
    }
}
