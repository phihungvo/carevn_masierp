package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.TemplateContract;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TemplateContract}, with proper type conversions.
 */
@Service
public class TemplateContractRowMapper implements BiFunction<Row, String, TemplateContract> {

    private final ColumnConverter converter;

    public TemplateContractRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TemplateContract} stored in the database.
     */
    @Override
    public TemplateContract apply(Row row, String prefix) {
        TemplateContract entity = new TemplateContract();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setNameFile(converter.fromRow(row, prefix + "_name_file", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
