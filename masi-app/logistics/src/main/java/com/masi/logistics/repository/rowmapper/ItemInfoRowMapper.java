package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.ItemInfo;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ItemInfo}, with proper type conversions.
 */
@Service
public class ItemInfoRowMapper implements BiFunction<Row, String, ItemInfo> {

    private final ColumnConverter converter;

    public ItemInfoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ItemInfo} stored in the database.
     */
    @Override
    public ItemInfo apply(Row row, String prefix) {
        ItemInfo entity = new ItemInfo();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setRegistrationNumber(converter.fromRow(row, prefix + "_registration_number", String.class));
        entity.setRegistrationDate(converter.fromRow(row, prefix + "_registration_date", ZonedDateTime.class));
        entity.setHandoverNumber(converter.fromRow(row, prefix + "_handover_number", String.class));
        entity.setHandoverDate(converter.fromRow(row, prefix + "_handover_date", ZonedDateTime.class));
        entity.setHandoverBy(converter.fromRow(row, prefix + "_handover_by", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", UUID.class));
        entity.setUserPosition(converter.fromRow(row, prefix + "_user_position", String.class));
        entity.setSeriesNumber(converter.fromRow(row, prefix + "_series_number", String.class));
        entity.setUsageDate(converter.fromRow(row, prefix + "_usage_date", ZonedDateTime.class));
        entity.setInvoiceNumber(converter.fromRow(row, prefix + "_invoice_number", String.class));
        entity.setInvoiceDate(converter.fromRow(row, prefix + "_invoice_date", ZonedDateTime.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", String.class));
        entity.setLiquidationDate(converter.fromRow(row, prefix + "_liquidation_date", ZonedDateTime.class));
        entity.setUnit(converter.fromRow(row, prefix + "_unit", UUID.class));
        entity.setYearOfUse(converter.fromRow(row, prefix + "_year_of_use", Integer.class));
        entity.setMonthOfUse(converter.fromRow(row, prefix + "_month_of_use", Integer.class));
        entity.setWarrantyPeriod(converter.fromRow(row, prefix + "_warranty_period", ZonedDateTime.class));
        entity.setManufacturer(converter.fromRow(row, prefix + "_manufacturer", String.class));
        entity.setIsMadeIn(converter.fromRow(row, prefix + "_is_made_in", Boolean.class));
        entity.setSpecs(converter.fromRow(row, prefix + "_specs", String.class));
        entity.setRemovalDate(converter.fromRow(row, prefix + "_removal_date", ZonedDateTime.class));
        entity.setReasonForRemoval(converter.fromRow(row, prefix + "_reason_for_removal", String.class));
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
        entity.setInventoryStorageId(converter.fromRow(row, prefix + "_inventory_storage_id", UUID.class));
        entity.setItemSubCategoryId(converter.fromRow(row, prefix + "_item_sub_category_id", UUID.class));
        entity.setHandoverById(converter.fromRow(row, prefix + "_handover_by_id", UUID.class));
        entity.setUserName(converter.fromRow(row, prefix + "_user_name", String.class));


        return entity;
    }
}
