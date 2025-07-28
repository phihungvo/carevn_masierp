package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.InventoriesCheckDetail;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link InventoriesCheckDetail}, with proper type conversions.
 */
@Service
public class InventoriesCheckDetailRowMapper implements BiFunction<Row, String, InventoriesCheckDetail> {

    private final ColumnConverter converter;

    public InventoriesCheckDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link InventoriesCheckDetail} stored in the database.
     */
    @Override
    public InventoriesCheckDetail apply(Row row, String prefix) {
        InventoriesCheckDetail entity = new InventoriesCheckDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setInventoriesCheckId(converter.fromRow(row, prefix + "_inventories_check_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setItemId(converter.fromRow(row, prefix + "_item_id", UUID.class));
        entity.setSystemQuantity(converter.fromRow(row, prefix + "_system_quantity", BigDecimal.class));
        entity.setActualQuantity(converter.fromRow(row, prefix + "_actual_quantity", BigDecimal.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        return entity;
    }
}
