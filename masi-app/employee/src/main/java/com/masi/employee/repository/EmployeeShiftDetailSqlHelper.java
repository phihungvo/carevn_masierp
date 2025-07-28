package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EmployeeShiftDetailSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("time_keeping_id", table, columnPrefix + "_time_keeping_id"));
        columns.add(Column.aliased("check_in_time", table, columnPrefix + "_check_in_time"));
        columns.add(Column.aliased("check_out_time", table, columnPrefix + "_check_out_time"));
        columns.add(Column.aliased("completion_percent", table, columnPrefix + "_completion_percent"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("shift_id", table, columnPrefix + "_shift_id"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));

        columns.add(Column.aliased("is_shift_off", table, columnPrefix + "_is_shift_off"));
        columns.add(Column.aliased("leave_day_type", table, columnPrefix + "_leave_day_type"));
        columns.add(Column.aliased("is_wfh", table, columnPrefix + "_is_wfh"));
        columns.add(Column.aliased("violation_id", table, columnPrefix + "_violation_id"));
        columns.add(Column.aliased("violation_type", table, columnPrefix + "_violation_type"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("is_paid_shift", table, columnPrefix + "_is_paid_shift"));


        return columns;
    }
}
