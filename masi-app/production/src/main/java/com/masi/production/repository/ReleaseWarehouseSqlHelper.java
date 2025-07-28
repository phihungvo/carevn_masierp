package com.masi.production.repository;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReleaseWarehouseSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("item_id", table, columnPrefix + "_item_id"));
        columns.add(Column.aliased("item_name", table, columnPrefix + "_item_name"));
        columns.add(Column.aliased("item_category_id", table, columnPrefix + "_item_category_id"));
        columns.add(Column.aliased("item_category_code", table, columnPrefix + "_item_category_code"));
        columns.add(Column.aliased("item_category_name", table, columnPrefix + "_item_category_name"));
        columns.add(Column.aliased("item_code", table, columnPrefix + "_item_code"));
        columns.add(Column.aliased("uom_id", table, columnPrefix + "_uom_id"));
        columns.add(Column.aliased("uom_name", table, columnPrefix + "_uom_name"));
        columns.add(Column.aliased("warehouse_id", table, columnPrefix + "_warehouse_id"));
        columns.add(Column.aliased("warehouse_name", table, columnPrefix + "_warehouse_name"));
        columns.add(Column.aliased("warehouse_type_id", table, columnPrefix + "_warehouse_type_id"));
        columns.add(Column.aliased("warehouse_type_name", table, columnPrefix + "_warehouse_type_name"));
        columns.add(Column.aliased("percent_protein", table, columnPrefix + "_percent_protein"));
        columns.add(Column.aliased("production_volume", table, columnPrefix + "_production_volume"));
        columns.add(Column.aliased("order_id", table, columnPrefix + "_order_id"));
        columns.add(Column.aliased("manufacture_order_id", table, columnPrefix + "_manufacture_order_id"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("production_standard_id", table, columnPrefix + "_production_standard_id"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));

        columns.add(Column.aliased("production_quantity", table, columnPrefix + "_production_quantity"));
        columns.add(Column.aliased("expire_date", table, columnPrefix + "_expire_date"));
        columns.add(Column.aliased("calculation_quantity", table, columnPrefix + "_calculation_quantity"));
        columns.add(Column.aliased("additive_material_checklist_id", table, columnPrefix + "_additive_material_checklist_id"));

        return columns;
    }
}
