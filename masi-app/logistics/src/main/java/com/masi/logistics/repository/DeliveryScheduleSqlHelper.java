package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;

import com.masi.logistics.domain.DeliverySchedule;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DeliveryScheduleSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("delivery_date", table, columnPrefix + "_delivery_date"));
        columns.add(Column.aliased("expected_receive_date", table, columnPrefix + "_expected_receive_date"));
        columns.add(Column.aliased("order_id", table, columnPrefix + "_order_id"));
        columns.add(Column.aliased("content", table, columnPrefix + "_content"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("unit_id", table, columnPrefix + "_unit_id"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));
        columns.add(Column.aliased("total", table, columnPrefix + "_total"));
        columns.add(Column.aliased("payment_method", table, columnPrefix + "_payment_method"));
        columns.add(Column.aliased("receiver_name", table, columnPrefix + "_receiver_name"));
        columns.add(Column.aliased("delivery_location", table, columnPrefix + "_delivery_location"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));


        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));

        return columns;
    }
}
