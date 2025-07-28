package com.masi.sale.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PurchaseRequestSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("request_status", table, columnPrefix + "_request_status"));
        columns.add(Column.aliased("product_name", table, columnPrefix + "_product_name"));
        columns.add(Column.aliased("unit", table, columnPrefix + "_unit"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("unit_price", table, columnPrefix + "_unit_price"));
        columns.add(Column.aliased("total_price", table, columnPrefix + "_total_price"));
        columns.add(Column.aliased("supplier", table, columnPrefix + "_supplier"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("create_date", table, columnPrefix + "_create_date"));
        columns.add(Column.aliased("updated_date", table, columnPrefix + "_updated_date"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));

        return columns;
    }
}
