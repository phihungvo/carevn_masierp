package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProductMaintainSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("product_batch_code", table, columnPrefix + "_product_batch_code"));
        columns.add(Column.aliased("product_batch_name", table, columnPrefix + "_product_batch_name"));
        columns.add(Column.aliased("manufacture_date", table, columnPrefix + "_manufacture_date"));
        columns.add(Column.aliased("expired_date", table, columnPrefix + "_expired_date"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated_at", table, columnPrefix + "_last_updated_at"));
        columns.add(Column.aliased("product_package_id", table, columnPrefix + "_product_package_id"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        return columns;
    }
}
