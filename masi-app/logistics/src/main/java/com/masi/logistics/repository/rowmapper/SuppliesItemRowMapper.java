package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.SuppliesItem;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SuppliesItem}, with proper type conversions.
 */
@Service
public class SuppliesItemRowMapper implements BiFunction<Row, String, SuppliesItem> {

    private final ColumnConverter converter;

    public SuppliesItemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SuppliesItem} stored in the database.
     */
    @Override
    public SuppliesItem apply(Row row, String prefix) {
        SuppliesItem entity = new SuppliesItem();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setIdSuppliesRequest(converter.fromRow(row, prefix + "_id_supplies_request", UUID.class));
        entity.setIdItem(converter.fromRow(row, prefix + "_id_item", UUID.class));
        entity.setIdUom(converter.fromRow(row, prefix + "_id_uom", UUID.class));
        entity.setSuppliesId(converter.fromRow(row, prefix + "_supplies_id", UUID.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", BigDecimal.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setImageIds(converter.fromRow(row, prefix + "_image_ids", String.class));
        entity.setBankInfo(converter.fromRow(row, prefix + "_bank_info", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setVat(converter.fromRow(row, prefix + "_vat", Double.class));
        entity.setTotalAmountAfterVat(converter.fromRow(row, prefix + "_total_amount_after_vat", BigDecimal.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", BigDecimal.class));
        entity.setVatId(converter.fromRow(row, prefix + "_vat_id", UUID.class));
        return entity;
    }
}
