package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;

import io.r2dbc.postgresql.codec.Json;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LeaveRegimeRequestSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("leave_type", table, columnPrefix + "_leave_type"));
        columns.add(Column.aliased("last_work_date", table, columnPrefix + "_last_work_date"));
        columns.add(Column.aliased("return_work_date", table, columnPrefix + "_return_work_date"));
        columns.add(Column.aliased("substitute_id", table, columnPrefix + "_substitute_id"));
        columns.add(Column.aliased("company_id", table, columnPrefix + "_company_id"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        columns.add(Column.aliased("leave_request_day_type", table, columnPrefix + "_leave_request_day_type"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("leave_request_id", table, columnPrefix + "_leave_request_id"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("file_id", table, columnPrefix + "_file_id"));
        columns.add(Column.aliased("file_name", table, columnPrefix + "_file_name"));
        columns.add(Column.aliased("from_time", table, columnPrefix + "_from_time"));
        columns.add(Column.aliased("to_time", table, columnPrefix + "_to_time"));
//        entity.setFiles(converter.fromRow(row, prefix + "_files", Json.class));
        columns.add(Column.aliased("files", table, columnPrefix + "_files"));
        columns.add(Column.aliased("total_day_off", table, columnPrefix + "_total_day_off"));
        return columns;
    }
}
