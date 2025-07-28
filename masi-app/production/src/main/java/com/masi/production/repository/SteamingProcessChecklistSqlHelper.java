package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SteamingProcessChecklistSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("check_date", table, columnPrefix + "_check_date"));
        columns.add(Column.aliased("check_time", table, columnPrefix + "_check_time"));
        columns.add(Column.aliased("weight_number", table, columnPrefix + "_weight_number"));
        columns.add(Column.aliased("steamer_atm", table, columnPrefix + "_steamer_atm"));
        columns.add(Column.aliased("steamer_temp", table, columnPrefix + "_steamer_temp"));
        columns.add(Column.aliased("steamer_time", table, columnPrefix + "_steamer_time"));
        columns.add(Column.aliased("tub_1_atm", table, columnPrefix + "_tub_1_atm"));
        columns.add(Column.aliased("tub_1_temp", table, columnPrefix + "_tub_1_temp"));
        columns.add(Column.aliased("tub_1_time", table, columnPrefix + "_tub_1_time"));
        columns.add(Column.aliased("tub_2_atm", table, columnPrefix + "_tub_2_atm"));
        columns.add(Column.aliased("tub_2_temp", table, columnPrefix + "_tub_2_temp"));
        columns.add(Column.aliased("tub_2_time", table, columnPrefix + "_tub_2_time"));
        columns.add(Column.aliased("fin_product_no", table, columnPrefix + "_fin_product_no"));
        columns.add(Column.aliased("receiver_id", table, columnPrefix + "_receiver_id"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));

        columns.add(Column.aliased("work_item_id", table, columnPrefix + "_work_item_id"));
        return columns;
    }
}
