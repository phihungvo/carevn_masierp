package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;

import com.masi.production.domain.enumeration.WorkOrderType;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class WorkOrderSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("from_date", table, columnPrefix + "_from_date"));
        columns.add(Column.aliased("to_date", table, columnPrefix + "_to_date"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));

        columns.add(Column.aliased("work_item_id", table, columnPrefix + "_work_item_id"));
        columns.add(Column.aliased("manufacture_order_id", table, columnPrefix + "_manufacture_order_id"));
        columns.add(Column.aliased("checklist_type", table, columnPrefix + "_checklist_type"));
        columns.add(Column.aliased("manufacture_order_type", table, columnPrefix + "_manufacture_order_type"));
        return columns;
    }
}
