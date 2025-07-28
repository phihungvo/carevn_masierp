package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.PurchaseRequestFile;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PurchaseRequestFile}, with proper type conversions.
 */
@Service
public class PurchaseRequestFileRowMapper implements BiFunction<Row, String, PurchaseRequestFile> {

    private final ColumnConverter converter;

    public PurchaseRequestFileRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PurchaseRequestFile} stored in the database.
     */
    @Override
    public PurchaseRequestFile apply(Row row, String prefix) {
        PurchaseRequestFile entity = new PurchaseRequestFile();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setFilePath(converter.fromRow(row, prefix + "_file_path", String.class));
        entity.setPurchaseRequestId(converter.fromRow(row, prefix + "_purchase_request_id", UUID.class));
        return entity;
    }
}
