package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProductionStandardSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("production_powder_qty", table, columnPrefix + "_production_powder_qty"));
        columns.add(Column.aliased("unit", table, columnPrefix + "_unit"));
        columns.add(Column.aliased("due_date", table, columnPrefix + "_due_date"));
        columns.add(Column.aliased("start_date", table, columnPrefix + "_start_date"));
        columns.add(Column.aliased("workspace", table, columnPrefix + "_workspace"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated_at", table, columnPrefix + "_last_updated_at"));
        columns.add(Column.aliased("material_Id", table, columnPrefix + "_material_Id"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        return columns;
    }
}
