package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.QuotationExport;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link QuotationExport}, with proper type conversions.
 */
@Service
public class QuotationExportRowMapper implements BiFunction<Row, String, QuotationExport> {

    private final ColumnConverter converter;

    public QuotationExportRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link QuotationExport} stored in the database.
     */
    @Override
    public QuotationExport apply(Row row, String prefix) {
        QuotationExport entity = new QuotationExport();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setFileId(converter.fromRow(row, prefix + "_file_id", String.class));
        entity.setFileName(converter.fromRow(row, prefix + "_file_name", String.class));

        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setDeletedDate(converter.fromRow(row, prefix + "_deleted_date", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setQuotationId(converter.fromRow(row, prefix + "_quotation_id", UUID.class));
        return entity;
    }
}
