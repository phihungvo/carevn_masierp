package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class InventoriesDetailSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("item_id", table, columnPrefix + "_item_id"));
        columns.add(Column.aliased("inventories_id", table, columnPrefix + "_inventories_id"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));
        columns.add(Column.aliased("total_price", table, columnPrefix + "_total_price"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("code_uom", table, columnPrefix + "_code_uom"));
        columns.add(Column.aliased("uom_id", table, columnPrefix + "_uom_id"));
        columns.add(Column.aliased("uom_name", table, columnPrefix + "_uom_name"));
        columns.add(Column.aliased("before_item_inventory", table, columnPrefix + "_before_item_inventory"));
        columns.add(Column.aliased("after_item_inventory", table, columnPrefix + "_after_item_inventory"));
        columns.add(Column.aliased("cost_price", table, columnPrefix + "_cost_price"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("vat_rate", table, columnPrefix + "_vat_rate"));
        columns.add(Column.aliased("vat_amount", table, columnPrefix + "_vat_amount"));
        columns.add(Column.aliased("vat_id", table, columnPrefix + "_vat_id"));


        columns.add(Column.aliased("register_date", table, columnPrefix + "_register_date"));
        columns.add(Column.aliased("depreciation_date", table, columnPrefix + "_depreciation_date"));
        columns.add(Column.aliased("department_id", table, columnPrefix + "_department_id"));
        columns.add(Column.aliased("usage_month", table, columnPrefix + "_usage_month"));
        columns.add(Column.aliased("holder", table, columnPrefix + "_holder"));
        columns.add(Column.aliased("depreciation_allocation", table, columnPrefix + "_depreciation_allocation"));
        columns.add(Column.aliased("expense_account", table, columnPrefix + "_expense_account"));
        columns.add(Column.aliased("cost_elements", table, columnPrefix + "_cost_elements"));
        columns.add(Column.aliased("unit_price", table, columnPrefix + "_unit_price"));
        return columns;
    }
}
