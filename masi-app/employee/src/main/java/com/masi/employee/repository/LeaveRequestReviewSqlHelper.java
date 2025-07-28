package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LeaveRequestReviewSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("reason", table, columnPrefix + "_reason"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));

        columns.add(Column.aliased("reviewer_id", table, columnPrefix + "_reviewer_id"));
        columns.add(Column.aliased("leave_request_id", table, columnPrefix + "_leave_request_id"));
        columns.add(Column.aliased("file_id", table, columnPrefix + "_file_id"));
        columns.add(Column.aliased("file_name", table, columnPrefix + "_file_name"));
        return columns;
    }
}
