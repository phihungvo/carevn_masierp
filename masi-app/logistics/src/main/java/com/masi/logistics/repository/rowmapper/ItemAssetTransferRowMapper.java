package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.ItemAssetTransfer;
import com.masi.logistics.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ItemAssetTransfer}, with proper type conversions.
 */
@Service
public class ItemAssetTransferRowMapper implements BiFunction<Row, String, ItemAssetTransfer> {

    private final ColumnConverter converter;

    public ItemAssetTransferRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ItemAssetTransfer} stored in the database.
     */
    @Override
    public ItemAssetTransfer apply(Row row, String prefix) {
        ItemAssetTransfer entity = new ItemAssetTransfer();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", Json  .class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", StatusEntity.class));
        entity.setInventoriesStorageId(converter.fromRow(row, prefix + "_inventories_storage_id", UUID.class));
        entity.setTransactionTypeId(converter.fromRow(row, prefix + "_transaction_type_id", UUID.class));
        entity.setItemCategoryId(converter.fromRow(row, prefix + "_item_category_id", UUID.class));
        entity.setTransferDate(converter.fromRow(row, prefix + "_transfer_date", ZonedDateTime.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setFromUnit(converter.fromRow(row, prefix + "_from_unit", String.class));
        entity.setFromDepartmentId(converter.fromRow(row, prefix + "_from_department_id", UUID.class));
        entity.setToDepartmentId(converter.fromRow(row, prefix + "_to_department_id", UUID.class));
        entity.setFromPersonId(converter.fromRow(row, prefix + "_from_person_id", UUID.class));
        entity.setToPersonId(converter.fromRow(row, prefix + "_to_person_id", UUID.class));
        entity.setFromAddress(converter.fromRow(row, prefix + "_from_address", String.class));
        entity.setToAddress(converter.fromRow(row, prefix + "_to_address", String.class));
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
