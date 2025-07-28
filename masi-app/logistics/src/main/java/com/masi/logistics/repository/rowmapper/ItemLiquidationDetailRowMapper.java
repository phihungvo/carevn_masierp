package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.ItemLiquidationDetail;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ItemLiquidationDetail}, with proper type conversions.
 */
@Service
public class ItemLiquidationDetailRowMapper implements BiFunction<Row, String, ItemLiquidationDetail> {

    private final ColumnConverter converter;

    public ItemLiquidationDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ItemLiquidationDetail} stored in the database.
     */
    @Override
    public ItemLiquidationDetail apply(Row row, String prefix) {
        ItemLiquidationDetail entity = new ItemLiquidationDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setLiquidationDate(converter.fromRow(row, prefix + "_liquidation_date", ZonedDateTime.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setItemLiquidationId(converter.fromRow(row, prefix + "_item_liquidation_id", UUID.class));
        entity.setInventoriesStorageId(converter.fromRow(row, prefix + "_inventories_storage_id", UUID.class));


        return entity;
    }
}
