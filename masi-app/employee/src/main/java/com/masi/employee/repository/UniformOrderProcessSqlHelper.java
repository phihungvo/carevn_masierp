package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UniformOrderProcessSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("approver_id", table, columnPrefix + "_approver_id"));
        columns.add(Column.aliased("reason", table, columnPrefix + "_reason"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("file_id", table, columnPrefix + "_file_id"));
        columns.add(Column.aliased("file_name", table, columnPrefix + "_file_name"));
        columns.add(Column.aliased("create_at", table, columnPrefix + "_create_at"));
        columns.add(Column.aliased("create_by", table, columnPrefix + "_create_by"));
        columns.add(Column.aliased("update_at", table, columnPrefix + "_update_at"));
        columns.add(Column.aliased("update_by", table, columnPrefix + "_update_by"));
        columns.add(Column.aliased("delete_at", table, columnPrefix + "_delete_at"));
        columns.add(Column.aliased("delete_by", table, columnPrefix + "_delete_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));

        columns.add(Column.aliased("uniform_order_id", table, columnPrefix + "_uniform_order_id"));
        return columns;
    }
}
