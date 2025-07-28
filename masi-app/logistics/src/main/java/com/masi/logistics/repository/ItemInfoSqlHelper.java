package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ItemInfoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("registration_number", table, columnPrefix + "_registration_number"));
        columns.add(Column.aliased("registration_date", table, columnPrefix + "_registration_date"));
        columns.add(Column.aliased("handover_number", table, columnPrefix + "_handover_number"));
        columns.add(Column.aliased("handover_date", table, columnPrefix + "_handover_date"));
        columns.add(Column.aliased("handover_by", table, columnPrefix + "_handover_by"));
        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        columns.add(Column.aliased("user_position", table, columnPrefix + "_user_position"));
        columns.add(Column.aliased("series_number", table, columnPrefix + "_series_number"));
        columns.add(Column.aliased("usage_date", table, columnPrefix + "_usage_date"));
        columns.add(Column.aliased("invoice_number", table, columnPrefix + "_invoice_number"));
        columns.add(Column.aliased("invoice_date", table, columnPrefix + "_invoice_date"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("liquidation_date", table, columnPrefix + "_liquidation_date"));
        columns.add(Column.aliased("unit", table, columnPrefix + "_unit"));
        columns.add(Column.aliased("year_of_use", table, columnPrefix + "_year_of_use"));
        columns.add(Column.aliased("month_of_use", table, columnPrefix + "_month_of_use"));
        columns.add(Column.aliased("warranty_period", table, columnPrefix + "_warranty_period"));
        columns.add(Column.aliased("manufacturer", table, columnPrefix + "_manufacturer"));
        columns.add(Column.aliased("is_made_in", table, columnPrefix + "_is_made_in"));
        columns.add(Column.aliased("specs", table, columnPrefix + "_specs"));
        columns.add(Column.aliased("removal_date", table, columnPrefix + "_removal_date"));
        columns.add(Column.aliased("reason_for_removal", table, columnPrefix + "_reason_for_removal"));
        columns.add(Column.aliased("attribute", table, columnPrefix + "_attribute"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("inventory_storage_id", table, columnPrefix + "_inventory_storage_id"));
        columns.add(Column.aliased("item_sub_category_id", table, columnPrefix + "_item_sub_category_id"));
        columns.add(Column.aliased("handover_by_id", table, columnPrefix + "_handover_by_id"));
        columns.add(Column.aliased("user_name", table, columnPrefix + "_user_name"));


        return columns;
    }
}
