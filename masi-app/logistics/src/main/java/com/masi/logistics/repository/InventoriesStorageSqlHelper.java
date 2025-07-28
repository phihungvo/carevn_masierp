package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class InventoriesStorageSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("item_id", table, columnPrefix + "_item_id"));
        columns.add(Column.aliased("inventories_detail_id", table, columnPrefix + "_inventories_detail_id"));
        columns.add(Column.aliased("import_date", table, columnPrefix + "_import_date"));
        columns.add(Column.aliased("export_date", table, columnPrefix + "_export_date"));
        columns.add(Column.aliased("depreciation", table, columnPrefix + "_depreciation"));
        columns.add(Column.aliased("expiry_date", table, columnPrefix + "_expiry_date"));
        columns.add(Column.aliased("notes", table, columnPrefix + "_notes"));
        columns.add(Column.aliased("attribute", table, columnPrefix + "_attribute"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("item_type", table, columnPrefix + "_item_type"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));
        columns.add(Column.aliased("remaining_price", table, columnPrefix + "_remaining_price"));
        columns.add(Column.aliased("warehouse_id", table, columnPrefix + "_warehouse_id"));
        columns.add(Column.aliased("asset_logs", table, columnPrefix + "_asset_logs"));
        columns.add(Column.aliased("supplier_id", table, columnPrefix + "_supplier_id"));
        return columns;
    }
}
