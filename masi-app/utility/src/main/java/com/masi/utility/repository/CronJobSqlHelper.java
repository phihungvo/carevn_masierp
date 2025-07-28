package com.masi.utility.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CronJobSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("action", table, columnPrefix + "_action"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("every_minute", table, columnPrefix + "_every_minute"));
        columns.add(Column.aliased("at_minute", table, columnPrefix + "_at_minute"));
        columns.add(Column.aliased("at_hour", table, columnPrefix + "_at_hour"));
        columns.add(Column.aliased("at_day_of_month", table, columnPrefix + "_at_day_of_month"));
        columns.add(Column.aliased("at_month", table, columnPrefix + "_at_month"));
        columns.add(Column.aliased("at_day_of_week", table, columnPrefix + "_at_day_of_week"));
        columns.add(Column.aliased("enabled", table, columnPrefix + "_enabled"));
        columns.add(Column.aliased("last_run", table, columnPrefix + "_last_run"));
        columns.add(Column.aliased("next_run", table, columnPrefix + "_next_run"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("url_db", table, columnPrefix + "_url_db"));
        columns.add(Column.aliased("user_db", table, columnPrefix + "_user_db"));
        columns.add(Column.aliased("pw_db", table, columnPrefix + "_pw_db"));
        columns.add(Column.aliased("zone_offset", table, columnPrefix + "_zone_offset"));

        return columns;
    }
}
