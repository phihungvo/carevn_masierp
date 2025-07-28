package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LeaveRequestSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("from_date", table, columnPrefix + "_from_date"));
        columns.add(Column.aliased("to_date", table, columnPrefix + "_to_date"));
        columns.add(Column.aliased("from_time", table, columnPrefix + "_from_time"));
        columns.add(Column.aliased("to_time", table, columnPrefix + "_to_time"));
        columns.add(Column.aliased("reason", table, columnPrefix + "_reason"));
        columns.add(Column.aliased("leave_request_type", table, columnPrefix + "_leave_request_type"));
        columns.add(Column.aliased("file_attachment", table, columnPrefix + "_file_attachment"));
        columns.add(Column.aliased("file_id", table, columnPrefix + "_file_id"));
        columns.add(Column.aliased("file_attachment_content_type", table, columnPrefix + "_file_attachment_content_type"));
        columns.add(Column.aliased("file_attachment_name", table, columnPrefix + "_file_attachment_name"));
        columns.add(Column.aliased("leave_request_day_type", table, columnPrefix + "_leave_request_day_type"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));

        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        columns.add(Column.aliased("substitute_id", table, columnPrefix + "_substitute_id"));
        columns.add(Column.aliased("files", table, columnPrefix + "_files"));
        columns.add(Column.aliased("total_day_off", table, columnPrefix + "_total_day_off"));
        return columns;
    }
}
