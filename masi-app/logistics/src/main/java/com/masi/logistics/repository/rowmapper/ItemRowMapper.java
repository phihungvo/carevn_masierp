package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.enumeration.ItemType;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Item}, with proper type conversions.
 */
@Service
public class ItemRowMapper implements BiFunction<Row, String, Item> {

    private final ColumnConverter converter;

    public ItemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Item} stored in the database.
     */
    @Override
    public Item apply(Row row, String prefix) {
        Item entity = new Item();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setUomId(converter.fromRow(row, prefix + "_uom_id", UUID.class));
        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", Json.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setItemCategoryId(converter.fromRow(row, prefix + "_item_category_id", UUID.class));
        entity.setPercentProtein(converter.fromRow(row, prefix + "_percent_protein", Float.class));
        entity.setNotes(converter.fromRow(row, prefix + "_notes", String.class));
        entity.setVatRate(converter.fromRow(row, prefix + "_vat_rate", Float.class));
        entity.setUnitPrice(converter.fromRow(row, prefix + "_unit_price", Float.class));
        entity.setRevenueGroupId(converter.fromRow(row, prefix + "_revenue_group", UUID.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));
        entity.setItemType(converter.fromRow(row, prefix + "_item_type", ItemType.class));

        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setVatId(converter.fromRow(row, prefix + "_vat_id", UUID.class));
        entity.setItemTypeId(converter.fromRow(row, prefix + "_item_type_id", UUID.class));
        entity.setIsSeparation(converter.fromRow(row, prefix + "_is_separation", Boolean.class));
        return entity;
    }
}
