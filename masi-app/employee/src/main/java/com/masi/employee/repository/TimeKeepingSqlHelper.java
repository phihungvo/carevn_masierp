package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TimeKeepingSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("first_check_in", table, columnPrefix + "_first_check_in"));
        columns.add(Column.aliased("last_check_in", table, columnPrefix + "_last_check_in"));
        columns.add(Column.aliased("hours_worked", table, columnPrefix + "_hours_worked"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("is_abnormal", table, columnPrefix + "_is_abnormal"));
        columns.add(Column.aliased("is_day_off", table, columnPrefix + "_is_day_off"));
        columns.add(Column.aliased("is_work_from_home", table, columnPrefix + "_is_work_from_home"));
        columns.add(Column.aliased("is_override", table, columnPrefix + "_is_override"));
        columns.add(Column.aliased("locked", table, columnPrefix + "_locked"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated_at", table, columnPrefix + "_last_updated_at"));
        columns.add(Column.aliased("leave_type", table, columnPrefix + "_leave_type"));
        columns.add(Column.aliased("leave_day_type", table, columnPrefix + "_leave_day_type"));
        columns.add(Column.aliased("violation_type", table, columnPrefix + "_violation_type"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        columns.add(Column.aliased("personal_monthly_timesheet_id", table, columnPrefix + "_personal_monthly_timesheet_id"));
        return columns;
    }
}
