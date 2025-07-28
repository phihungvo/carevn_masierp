package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.InventoriesCheck;
import com.masi.logistics.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link InventoriesCheck}, with proper type conversions.
 */
@Service
public class InventoriesCheckRowMapper implements BiFunction<Row, String, InventoriesCheck> {

    private final ColumnConverter converter;

    public InventoriesCheckRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link InventoriesCheck} stored in the database.
     */
    @Override
    public InventoriesCheck apply(Row row, String prefix) {
        InventoriesCheck entity = new InventoriesCheck();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setCheckDate(converter.fromRow(row, prefix + "_check_date", ZonedDateTime.class));
        entity.setWarehouseId(converter.fromRow(row, prefix + "_warehouse", UUID.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setApprover1(converter.fromRow(row, prefix + "_approver_1", UUID.class));
        entity.setApprover2(converter.fromRow(row, prefix + "_approver_2", UUID.class));
        entity.setApprover3(converter.fromRow(row, prefix + "_approver_3", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", StatusEntity.class));
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
        entity.setAmountOfDifference(converter.fromRow(row, prefix + "_amount_of_difference", BigDecimal.class));
        entity.setWarehouseId(converter.fromRow(row, prefix + "_warehouse_id", UUID.class));
        entity.setAttachment(converter.fromRow(row, prefix + "_attachment", Json.class));
        return entity;
    }
}
