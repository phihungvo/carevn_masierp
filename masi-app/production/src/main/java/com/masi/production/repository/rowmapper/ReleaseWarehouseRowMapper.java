package com.masi.production.repository.rowmapper;

import com.masi.production.domain.ReleaseWarehouse;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Converter between {@link Row} to {@link ReleaseWarehouse}, with proper type conversions.
 */
@Service
public class ReleaseWarehouseRowMapper implements BiFunction<Row, String, ReleaseWarehouse> {

    private final ColumnConverter converter;

    public ReleaseWarehouseRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReleaseWarehouse} stored in the database.
     */
    @Override
    public ReleaseWarehouse apply(Row row, String prefix) {
        ReleaseWarehouse entity = new ReleaseWarehouse();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setItemId(converter.fromRow(row, prefix + "_item_id", UUID.class));
        entity.setItemName(converter.fromRow(row, prefix + "_item_name", String.class));
        entity.setItemCategoryId(converter.fromRow(row, prefix + "_item_category_id", UUID.class));
        entity.setItemCategoryCode(converter.fromRow(row, prefix + "_item_category_code", String.class));
        entity.setItemCategoryName(converter.fromRow(row, prefix + "_item_category_name", String.class));
        entity.setItemCode(converter.fromRow(row, prefix + "_item_code", String.class));
        entity.setUomId(converter.fromRow(row, prefix + "_uom_id", UUID.class));
        entity.setUomName(converter.fromRow(row, prefix + "_uom_name", String.class));
        entity.setWarehouseId(converter.fromRow(row, prefix + "_warehouse_id", UUID.class));
        entity.setWarehouseName(converter.fromRow(row, prefix + "_warehouse_name", String.class));
        entity.setWarehouseTypeId(converter.fromRow(row, prefix + "_warehouse_type_id", UUID.class));
        entity.setWarehouseTypeName(converter.fromRow(row, prefix + "_warehouse_type_name", String.class));
        entity.setPercentProtein(converter.fromRow(row, prefix + "_percent_protein", Float.class));
        entity.setProductionVolume(converter.fromRow(row, prefix + "_production_volume", Float.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", UUID.class));
        entity.setManufactureOrderId(converter.fromRow(row, prefix + "_manufacture_order_id", UUID.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setProductionStandardId(converter.fromRow(row, prefix + "_production_standard_id", UUID.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Float.class));

        entity.setProductionQuantity(converter.fromRow(row, prefix + "_production_quantity", Float.class));
        entity.setExpireDate(converter.fromRow(row, prefix + "_expire_date", LocalDate.class));
        entity.setCalculationQuantity(converter.fromRow(row, prefix + "_calculation_quantity", Float.class));
        entity.setAdditiveMaterialChecklistId(converter.fromRow(row, prefix + "_additive_material_checklist_id", UUID.class));


        return entity;
    }
}
