package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.DocumentSequence;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DocumentSequence}, with proper type conversions.
 */
@Service
public class DocumentSequenceRowMapper implements BiFunction<Row, String, DocumentSequence> {

    private final ColumnConverter converter;

    public DocumentSequenceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DocumentSequence} stored in the database.
     */
    @Override
    public DocumentSequence apply(Row row, String prefix) {
        DocumentSequence entity = new DocumentSequence();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEntityName(converter.fromRow(row, prefix + "_entity_name", String.class));
        entity.setCurrentSequence(converter.fromRow(row, prefix + "_current_sequence", Integer.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        return entity;
    }
}
