package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MachineOperationChecklistSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("check_time", table, columnPrefix + "_check_time"));
        columns.add(Column.aliased("incinerator_air_duct", table, columnPrefix + "_incinerator_air_duct"));
        columns.add(Column.aliased("incinerator_air_duct_note", table, columnPrefix + "_incinerator_air_duct_note"));
        columns.add(Column.aliased("incinerator", table, columnPrefix + "_incinerator"));
        columns.add(Column.aliased("incinerator_check_note", table, columnPrefix + "_incinerator_check_note"));
        columns.add(Column.aliased("drying_oven_air_duct", table, columnPrefix + "_drying_oven_air_duct"));
        columns.add(Column.aliased("drying_oven_air_duct_note", table, columnPrefix + "_drying_oven_air_duct_note"));
        columns.add(Column.aliased("drying_oven_meter", table, columnPrefix + "_drying_oven_meter"));
        columns.add(Column.aliased("drying_oven_meter_note", table, columnPrefix + "_drying_oven_meter_note"));
        columns.add(Column.aliased("drying_oven_wall", table, columnPrefix + "_drying_oven_wall"));
        columns.add(Column.aliased("drying_oven_wall_note", table, columnPrefix + "_drying_oven_wall_note"));
        columns.add(Column.aliased("drying_oven_valve", table, columnPrefix + "_drying_oven_valve"));
        columns.add(Column.aliased("drying_oven_valve_note", table, columnPrefix + "_drying_oven_valve_note"));
        columns.add(Column.aliased("sieve_screen", table, columnPrefix + "_sieve_screen"));
        columns.add(Column.aliased("sieve_screen_note", table, columnPrefix + "_sieve_screen_note"));
        columns.add(Column.aliased("crusher", table, columnPrefix + "_crusher"));
        columns.add(Column.aliased("crusher_note", table, columnPrefix + "_crusher_note"));
        columns.add(Column.aliased("magnet", table, columnPrefix + "_magnet"));
        columns.add(Column.aliased("magnet_note", table, columnPrefix + "_magnet_note"));
        columns.add(Column.aliased("mixer", table, columnPrefix + "_mixer"));
        columns.add(Column.aliased("mixer_note", table, columnPrefix + "_mixer_note"));
        columns.add(Column.aliased("packaging_machine", table, columnPrefix + "_packaging_machine"));
        columns.add(Column.aliased("packaging_machine_note", table, columnPrefix + "_packaging_machine_note"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));

        columns.add(Column.aliased("check_date", table, columnPrefix + "_check_date"));
        columns.add(Column.aliased("work_item_id", table, columnPrefix + "_work_item_id"));
        return columns;
    }
}
