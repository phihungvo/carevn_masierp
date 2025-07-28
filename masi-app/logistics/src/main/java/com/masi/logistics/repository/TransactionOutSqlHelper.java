package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TransactionOutSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("transaction_id", table, columnPrefix + "_transaction_id"));
        columns.add(Column.aliased("transaction_code", table, columnPrefix + "_transaction_code"));
        columns.add(Column.aliased("unit_price", table, columnPrefix + "_unit_price"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("remain", table, columnPrefix + "_remain"));
        columns.add(Column.aliased("notes", table, columnPrefix + "_notes"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("expired_date", table, columnPrefix + "_expired_date"));
        columns.add(Column.aliased("manufacture_date", table, columnPrefix + "_manufacture_date"));
        columns.add(Column.aliased("item_id", table, columnPrefix + "_item_id"));
        columns.add(Column.aliased("item_code", table, columnPrefix + "_item_code"));
        columns.add(Column.aliased("warehouse_id", table, columnPrefix + "_warehouse_id"));
        columns.add(Column.aliased("warehouse_code", table, columnPrefix + "_warehouse_code"));
        columns.add(Column.aliased("supplier_id", table, columnPrefix + "_supplier_id"));
        columns.add(Column.aliased("supplier_code", table, columnPrefix + "_supplier_code"));
        columns.add(Column.aliased("customer_id", table, columnPrefix + "_customer_id"));
        columns.add(Column.aliased("customer_code", table, columnPrefix + "_customer_code"));
        columns.add(Column.aliased("order_id", table, columnPrefix + "_order_id"));
        columns.add(Column.aliased("order_code", table, columnPrefix + "_order_code"));
        columns.add(Column.aliased("manufacture_id", table, columnPrefix + "_manufacture_id"));
        columns.add(Column.aliased("manufacture_code", table, columnPrefix + "_manufacture_code"));
        columns.add(Column.aliased("packing_id", table, columnPrefix + "_packing_id"));
        columns.add(Column.aliased("packing_code", table, columnPrefix + "_packing_code"));
        columns.add(Column.aliased("create_at", table, columnPrefix + "_create_at"));
        columns.add(Column.aliased("create_by", table, columnPrefix + "_create_by"));
        columns.add(Column.aliased("update_at", table, columnPrefix + "_update_at"));
        columns.add(Column.aliased("update_by", table, columnPrefix + "_update_by"));
        columns.add(Column.aliased("delete_at", table, columnPrefix + "_delete_at"));
        columns.add(Column.aliased("delete_by", table, columnPrefix + "_delete_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));

        return columns;
    }
}
