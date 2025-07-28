package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProductRoutingSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("unit", table, columnPrefix + "_unit"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_updated_at", table, columnPrefix + "_last_updated_at"));

        columns.add(Column.aliased("factory_id", table, columnPrefix + "_factory_id"));
        columns.add(Column.aliased("storage_id", table, columnPrefix + "_storage_id"));
        columns.add(Column.aliased("product_maintain_id", table, columnPrefix + "_product_maintain_id"));
        columns.add(Column.aliased("warehouse_date", table, columnPrefix + "_warehouse_date"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        return columns;
    }
}
