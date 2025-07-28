package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AdditiveMaterialChecklistSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("check_date", table, columnPrefix + "_check_date"));
        columns.add(Column.aliased("check_time", table, columnPrefix + "_check_time"));
        columns.add(Column.aliased("weight_number", table, columnPrefix + "_weight_number"));
        columns.add(Column.aliased("check_impurity", table, columnPrefix + "_check_impurity"));
        columns.add(Column.aliased("impurity_note", table, columnPrefix + "_impurity_note"));
        columns.add(Column.aliased("weight_material", table, columnPrefix + "_weight_material"));
        columns.add(Column.aliased("weight_material_unit", table, columnPrefix + "_weight_material_unit"));
        columns.add(Column.aliased("bicabonat_lot_number", table, columnPrefix + "_bicabonat_lot_number"));
        columns.add(Column.aliased("bicacbonat_weight", table, columnPrefix + "_bicacbonat_weight"));
        columns.add(Column.aliased("bicacbonat_weight_unit", table, columnPrefix + "_bicacbonat_weight_unit"));
        columns.add(Column.aliased("receiver_id", table, columnPrefix + "_receiver_id"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));

        columns.add(Column.aliased("work_item_id", table, columnPrefix + "_work_item_id"));
        return columns;
    }
}
