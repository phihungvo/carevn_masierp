package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.AssetTransferDetails;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AssetTransferDetails}, with proper type conversions.
 */
@Service
public class AssetTransferDetailsRowMapper implements BiFunction<Row, String, AssetTransferDetails> {

    private final ColumnConverter converter;

    public AssetTransferDetailsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AssetTransferDetails} stored in the database.
     */
    @Override
    public AssetTransferDetails apply(Row row, String prefix) {
        AssetTransferDetails entity = new AssetTransferDetails();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", Json.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", String.class));
        entity.setInventoriesStorageId(converter.fromRow(row, prefix + "_inventories_storage_id", UUID.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", BigDecimal.class));
        entity.setNotes(converter.fromRow(row, prefix + "_notes", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setItemAssetTransferId(converter.fromRow(row, prefix + "_item_asset_transfer_id", UUID.class));

        entity.setEmployeeToId(converter.fromRow(row, prefix + "_employee_to_id", UUID.class));
        entity.setEmployeeFromId(converter.fromRow(row, prefix + "_employee_from_id", UUID.class));
        return entity;
    }
}
