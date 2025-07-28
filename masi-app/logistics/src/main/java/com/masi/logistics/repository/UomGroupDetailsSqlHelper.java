package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UomGroupDetailsSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("base_qty", table, columnPrefix + "_base_qty"));
        columns.add(Column.aliased("alt_qty", table, columnPrefix + "_alt_qty"));
        columns.add(Column.aliased("active", table, columnPrefix + "_active"));
        columns.add(Column.aliased("create_at", table, columnPrefix + "_create_at"));
        columns.add(Column.aliased("create_by", table, columnPrefix + "_create_by"));
        columns.add(Column.aliased("update_at", table, columnPrefix + "_update_at"));
        columns.add(Column.aliased("update_by", table, columnPrefix + "_update_by"));
        columns.add(Column.aliased("delete_at", table, columnPrefix + "_delete_at"));
        columns.add(Column.aliased("delete_by", table, columnPrefix + "_delete_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));

        columns.add(Column.aliased("base_uom_id", table, columnPrefix + "_base_uom_id"));
        columns.add(Column.aliased("alt_uom_id", table, columnPrefix + "_alt_uom_id"));
        columns.add(Column.aliased("uom_group_id", table, columnPrefix + "_uom_group_id"));
        return columns;
    }
}
