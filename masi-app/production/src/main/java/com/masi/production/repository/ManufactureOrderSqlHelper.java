package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ManufactureOrderSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("from_date", table, columnPrefix + "_from_date"));
        columns.add(Column.aliased("type_protein", table, columnPrefix + "_type_protein"));
        columns.add(Column.aliased("to_date", table, columnPrefix + "_to_date"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("order_id", table, columnPrefix + "_order_id"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("quality_check_sample_id", table, columnPrefix + "_quality_check_sample_id"));
        columns.add(Column.aliased("production_status", table, columnPrefix + "_production_status"));
        columns.add(Column.aliased("production_standard_id", table, columnPrefix + "_production_standard_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("production_quantity", table, columnPrefix + "_production_quantity"));

        columns.add(Column.aliased("material_id", table, columnPrefix + "_material_id"));
        columns.add(Column.aliased("percent_protein", table, columnPrefix + "_percent_protein"));
        columns.add(Column.aliased("manufacture_order_type", table, columnPrefix + "_manufacture_order_type"));
        columns.add(Column.aliased("attributes", table, columnPrefix + "_attributes"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("product_package_id", table, columnPrefix + "_product_package_id"));
        columns.add(Column.aliased("product_maintain_id", table, columnPrefix + "_product_maintain_id"));

        columns.add(Column.aliased("production_routing_id", table, columnPrefix + "_production_routing_id"));
        columns.add(Column.aliased("inventory_id", table, columnPrefix + "_inventory_id"));
        columns.add(Column.aliased("order_item_id", table, columnPrefix + "_order_item_id"));

        return columns;
    }
}
