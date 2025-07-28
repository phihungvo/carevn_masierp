package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.DocumentCodeSequence;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DocumentCodeSequence}, with proper type conversions.
 */
@Service
public class DocumentCodeSequenceRowMapper implements BiFunction<Row, String, DocumentCodeSequence> {

    private final ColumnConverter converter;

    public DocumentCodeSequenceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DocumentCodeSequence} stored in the database.
     */
    @Override
    public DocumentCodeSequence apply(Row row, String prefix) {
        DocumentCodeSequence entity = new DocumentCodeSequence();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setCurrentSequence(converter.fromRow(row, prefix + "_current_sequence", Integer.class));
        entity.setJavaFormat(converter.fromRow(row, prefix + "_java_format", String.class));
        entity.setDocumentType(converter.fromRow(row, prefix + "_document_type", String.class));
        return entity;
    }
}
