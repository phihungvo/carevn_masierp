package com.masi.production.repository.rowmapper;

import com.masi.production.domain.ProductionStandard;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ProductionStandard}, with proper type conversions.
 */
@Service
public class ProductionStandardRowMapper implements BiFunction<Row, String, ProductionStandard> {

    private final ColumnConverter converter;

    public ProductionStandardRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProductionStandard} stored in the database.
     */
    @Override
    public ProductionStandard apply(Row row, String prefix) {
        ProductionStandard entity = new ProductionStandard();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Float.class));
        entity.setProductionPowderQty(converter.fromRow(row, prefix + "_production_powder_qty", Float.class));
        entity.setUnit(converter.fromRow(row, prefix + "_unit", String.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", LocalDate.class));
        entity.setDueDate(converter.fromRow(row, prefix + "_due_date", LocalDate.class));
        entity.setWorkspace(converter.fromRow(row, prefix + "_workspace", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdatedAt(converter.fromRow(row, prefix + "_last_updated_at", ZonedDateTime.class));
        entity.setMaterialId(converter.fromRow(row, prefix + "_material_Id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", String.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));

        return entity;
    }
}
