package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.InventoriesStorage;
import com.masi.logistics.domain.enumeration.ItemStatus;
import com.masi.logistics.domain.enumeration.ItemType;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link InventoriesStorage}, with proper type conversions.
 */
@Service
public class InventoriesStorageRowMapper implements BiFunction<Row, String, InventoriesStorage> {

    private final ColumnConverter converter;

    public InventoriesStorageRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link InventoriesStorage} stored in the database.
     */
    @Override
    public InventoriesStorage apply(Row row, String prefix) {
        InventoriesStorage entity = new InventoriesStorage();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setItemId(converter.fromRow(row, prefix + "_item_id", UUID.class));
        entity.setInventoriesDetailId(converter.fromRow(row, prefix + "_inventories_detail_id", UUID.class));
        entity.setImportDate(converter.fromRow(row, prefix + "_import_date", ZonedDateTime.class));
        entity.setExportDate(converter.fromRow(row, prefix + "_export_date", ZonedDateTime.class));
        entity.setDepreciation(converter.fromRow(row, prefix + "_depreciation", String.class));
        entity.setExpiryDate(converter.fromRow(row, prefix + "_expiry_date", ZonedDateTime.class));
        entity.setNotes(converter.fromRow(row, prefix + "_notes", String.class));
        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", Json.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setItemType(converter.fromRow(row, prefix + "_item_type", ItemType.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", BigDecimal.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ItemStatus.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        entity.setRemainingPrice(converter.fromRow(row, prefix + "_remaining_price", BigDecimal.class));
        entity.setWarehouseId(converter.fromRow(row, prefix + "_warehouse_id", UUID.class));
        entity.setAssetLogs(converter.fromRow(row, prefix + "_asset_logs", Json.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));
        return entity;
    }
}
