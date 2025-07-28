package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ShiftSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_standard_work_schedule_config", table, columnPrefix + "_id_standard_work_schedule_config"));
        columns.add(Column.aliased("shift_name", table, columnPrefix + "_shift_name"));
        columns.add(Column.aliased("duration_hours", table, columnPrefix + "_duration_hours"));
        columns.add(Column.aliased("hour_start_time", table, columnPrefix + "_hour_start_time"));
        columns.add(Column.aliased("minute_start_time", table, columnPrefix + "_minute_start_time"));
        columns.add(Column.aliased("second_start_time", table, columnPrefix + "_second_start_time"));
        columns.add(Column.aliased("hour_end_time", table, columnPrefix + "_hour_end_time"));
        columns.add(Column.aliased("minute_end_time", table, columnPrefix + "_minute_end_time"));
        columns.add(Column.aliased("second_end_time", table, columnPrefix + "_second_end_time"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));

        return columns;
    }
}
