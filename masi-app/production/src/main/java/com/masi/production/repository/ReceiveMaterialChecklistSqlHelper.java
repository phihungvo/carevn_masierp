package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;

import com.masi.production.domain.enumeration.MaterialType;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ReceiveMaterialChecklistSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("check_date", table, columnPrefix + "_check_date"));
        columns.add(Column.aliased("check_time", table, columnPrefix + "_check_time"));
        columns.add(Column.aliased("weight_number", table, columnPrefix + "_weight_number"));
        columns.add(Column.aliased("transport_condition", table, columnPrefix + "_transport_condition"));
        columns.add(Column.aliased("transport_note", table, columnPrefix + "_transport_note"));
        columns.add(Column.aliased("check_status", table, columnPrefix + "_check_status"));
        columns.add(Column.aliased("status_note", table, columnPrefix + "_status_note"));
        columns.add(Column.aliased("check_smell", table, columnPrefix + "_check_smell"));
        columns.add(Column.aliased("smell_note", table, columnPrefix + "_smell_note"));
        columns.add(Column.aliased("check_impurity", table, columnPrefix + "_check_impurity"));
        columns.add(Column.aliased("impurity_note", table, columnPrefix + "_impurity_note"));
        columns.add(Column.aliased("check_poison", table, columnPrefix + "_check_poison"));
        columns.add(Column.aliased("poison_note", table, columnPrefix + "_poison_note"));
        columns.add(Column.aliased("receiver_id", table, columnPrefix + "_receiver_id"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("material_type", table, columnPrefix + "_material_type"));
        columns.add(Column.aliased("work_item_id", table, columnPrefix + "_work_item_id"));
        columns.add(Column.aliased("weight", table, columnPrefix + "_weight"));
        return columns;
    }
}
