package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.ItemCategory;
import com.masi.logistics.domain.enumeration.ItemTypeCategory;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ItemCategory}, with proper type conversions.
 */
@Service
public class ItemCategoryRowMapper implements BiFunction<Row, String, ItemCategory> {

    private final ColumnConverter converter;

    public ItemCategoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ItemCategory} stored in the database.
     */
    @Override
    public ItemCategory apply(Row row, String prefix) {
        ItemCategory entity = new ItemCategory();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setWarehouseTypeId(converter.fromRow(row, prefix + "_warehouse_type_id", UUID.class));
        entity.setItemTypeCategory(converter.fromRow(row, prefix + "_type_item_category", ItemTypeCategory.class));

        return entity;
    }
}
