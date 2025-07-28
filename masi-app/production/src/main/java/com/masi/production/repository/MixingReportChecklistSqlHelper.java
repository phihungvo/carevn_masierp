package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MixingReportChecklistSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("check_date", table, columnPrefix + "_check_date"));
        columns.add(Column.aliased("fin_product_1_no", table, columnPrefix + "_fin_product_1_no"));
        columns.add(Column.aliased("fin_product_1_weight", table, columnPrefix + "_fin_product_1_weight"));
        columns.add(Column.aliased("fin_product_1_weight_unit", table, columnPrefix + "_fin_product_1_weight_unit"));
        columns.add(Column.aliased("fin_product_2_no", table, columnPrefix + "_fin_product_2_no"));
        columns.add(Column.aliased("fin_product_2_weight", table, columnPrefix + "_fin_product_2_weight"));
        columns.add(Column.aliased("fin_product_2_weight_unit", table, columnPrefix + "_fin_product_2_weight_unit"));
        columns.add(Column.aliased("bht_no", table, columnPrefix + "_bht_no"));
        columns.add(Column.aliased("bht_weight", table, columnPrefix + "_bht_weight"));
        columns.add(Column.aliased("bht_weight_unit", table, columnPrefix + "_bht_weight_unit"));
        columns.add(Column.aliased("bht_weight_prd", table, columnPrefix + "_bht_weight_prd"));
        columns.add(Column.aliased("bht_weight_prd_unit", table, columnPrefix + "_bht_weight_prd_unit"));
        columns.add(Column.aliased("weight_prd_no", table, columnPrefix + "_weight_prd_no"));
        columns.add(Column.aliased("check_impurity", table, columnPrefix + "_check_impurity"));
        columns.add(Column.aliased("check_impurity_note", table, columnPrefix + "_check_impurity_note"));
        columns.add(Column.aliased("check_smell", table, columnPrefix + "_check_smell"));
        columns.add(Column.aliased("check_smell_note", table, columnPrefix + "_check_smell_note"));
        columns.add(Column.aliased("check_color", table, columnPrefix + "_check_color"));
        columns.add(Column.aliased("check_color_note", table, columnPrefix + "_check_color_note"));
        columns.add(Column.aliased("check_employee_id", table, columnPrefix + "_check_employee_id"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("moisture", table, columnPrefix + "_moisture"));
        columns.add(Column.aliased("tvn", table, columnPrefix + "_tvn"));
        columns.add(Column.aliased("ash", table, columnPrefix + "_ash"));
        columns.add(Column.aliased("protein", table, columnPrefix + "_protein"));

        columns.add(Column.aliased("work_item_id", table, columnPrefix + "_work_item_id"));
        return columns;
    }
}
