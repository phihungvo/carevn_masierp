package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class QualityCheckSampleSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("sampling_date", table, columnPrefix + "_sampling_date"));
        columns.add(Column.aliased("sample_no", table, columnPrefix + "_sample_no"));
        columns.add(Column.aliased("product_type", table, columnPrefix + "_product_type"));
        columns.add(Column.aliased("sample_weight", table, columnPrefix + "_sample_weight"));
        columns.add(Column.aliased("customer", table, columnPrefix + "_customer"));
        columns.add(Column.aliased("reason", table, columnPrefix + "_reason"));
        columns.add(Column.aliased("sample_release_date", table, columnPrefix + "_sample_release_date"));
        columns.add(Column.aliased("internal_hum", table, columnPrefix + "_internal_hum"));
        columns.add(Column.aliased("internal_tvn", table, columnPrefix + "_internal_tvn"));
        columns.add(Column.aliased("internal_ash", table, columnPrefix + "_internal_ash"));
        columns.add(Column.aliased("internal_protein", table, columnPrefix + "_internal_protein"));
        columns.add(Column.aliased("external_hum", table, columnPrefix + "_external_hum"));
        columns.add(Column.aliased("external_tvn", table, columnPrefix + "_external_tvn"));
        columns.add(Column.aliased("external_ash", table, columnPrefix + "_external_ash"));
        columns.add(Column.aliased("external_protein", table, columnPrefix + "_external_protein"));
        columns.add(Column.aliased("sampling_employee_id", table, columnPrefix + "_sampling_employee_id"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));

        columns.add(Column.aliased("disposal_id", table, columnPrefix + "_disposal_id"));
        columns.add(Column.aliased("manufacture_order_id", table, columnPrefix + "_manufacture_order_id"));
        columns.add(Column.aliased("package_id", table, columnPrefix + "_package_id"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("protein_percentage", table, columnPrefix + "_protein_percentage"));
        columns.add(Column.aliased("attributes", table, columnPrefix + "_attributes"));
        columns.add(Column.aliased("item_id", table, columnPrefix + "_item_id"));
        columns.add(Column.aliased("protein_percentage_apply", table, columnPrefix + "_protein_percentage_apply"));



        return columns;
    }
}
