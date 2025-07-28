package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MetalDetectionChecklistSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("check_date", table, columnPrefix + "_check_date"));
        columns.add(Column.aliased("fin_product_no", table, columnPrefix + "_fin_product_no"));
        columns.add(Column.aliased("magnet_begin", table, columnPrefix + "_magnet_begin"));
        columns.add(Column.aliased("magnet_begin_note", table, columnPrefix + "_magnet_begin_note"));
        columns.add(Column.aliased("magnet_end", table, columnPrefix + "_magnet_end"));
        columns.add(Column.aliased("magnet_end_note", table, columnPrefix + "_magnet_end_note"));
        columns.add(Column.aliased("screen_4_begin", table, columnPrefix + "_screen_4_begin"));
        columns.add(Column.aliased("screen_4_begin_note", table, columnPrefix + "_screen_4_begin_note"));
        columns.add(Column.aliased("screen_4_end", table, columnPrefix + "_screen_4_end"));
        columns.add(Column.aliased("screen_4_end_note", table, columnPrefix + "_screen_4_end_note"));
        columns.add(Column.aliased("screen_3_begin", table, columnPrefix + "_screen_3_begin"));
        columns.add(Column.aliased("screen_3_begin_note", table, columnPrefix + "_screen_3_begin_note"));
        columns.add(Column.aliased("screen_3_end", table, columnPrefix + "_screen_3_end"));
        columns.add(Column.aliased("screen_3_end_note", table, columnPrefix + "_screen_3_end_note"));
        columns.add(Column.aliased("checked_by", table, columnPrefix + "_checked_by"));
        columns.add(Column.aliased("audited_by", table, columnPrefix + "_audited_by"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));

        columns.add(Column.aliased("work_item_id", table, columnPrefix + "_work_item_id"));
        return columns;
    }
}
