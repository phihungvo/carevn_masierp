package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.ItemLiquidation;
import com.masi.logistics.domain.enumeration.LiquidationReason;
import com.masi.logistics.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ItemLiquidation}, with proper type conversions.
 */
@Service
public class ItemLiquidationRowMapper implements BiFunction<Row, String, ItemLiquidation> {

    private final ColumnConverter converter;

    public ItemLiquidationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ItemLiquidation} stored in the database.
     */
    @Override
    public ItemLiquidation apply(Row row, String prefix) {
        ItemLiquidation entity = new ItemLiquidation();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", Json.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", StatusEntity.class));
        entity.setLiquidationDate(converter.fromRow(row, prefix + "_liquidation_date", ZonedDateTime.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", LiquidationReason.class));
        entity.setPersonnelList(converter.fromRow(row, prefix + "_personnel_list", Json.class));
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
