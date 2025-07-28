package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LeaveDaySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("leave_request_id", table, columnPrefix + "_leave_request_id"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("leave_request_day_type", table, columnPrefix + "_leave_request_day_type"));
        columns.add(Column.aliased("leave_type", table, columnPrefix + "_leave_type"));
        columns.add(Column.aliased("is_locked", table, columnPrefix + "_is_locked"));

        return columns;
    }
}
