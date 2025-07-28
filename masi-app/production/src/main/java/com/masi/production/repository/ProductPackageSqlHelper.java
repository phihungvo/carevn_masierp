package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProductPackageSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("package_code", table, columnPrefix + "_package_code"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("unit", table, columnPrefix + "_unit"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated_at", table, columnPrefix + "_last_updated_at"));

        columns.add(Column.aliased("work_order_id", table, columnPrefix + "_work_order_id"));
        columns.add(Column.aliased("manufacture_order_id", table, columnPrefix + "_manufacture_order_id"));
        columns.add(Column.aliased("material_id", table, columnPrefix + "_material_id"));
        columns.add(Column.aliased("is_sew", table, columnPrefix + "_is_sew"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("weight", table, columnPrefix + "_weight"));
        columns.add(Column.aliased("package_by", table, columnPrefix + "_package_by"));
        columns.add(Column.aliased("package_at", table, columnPrefix + "_package_at"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        return columns;
    }
}
