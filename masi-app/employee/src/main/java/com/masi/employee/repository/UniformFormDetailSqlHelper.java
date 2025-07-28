package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UniformFormDetailSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("returned_quantity", table, columnPrefix + "_returned_quantity"));
        columns.add(Column.aliased("create_at", table, columnPrefix + "_create_at"));
        columns.add(Column.aliased("create_by", table, columnPrefix + "_create_by"));
        columns.add(Column.aliased("update_at", table, columnPrefix + "_update_at"));
        columns.add(Column.aliased("update_by", table, columnPrefix + "_update_by"));
        columns.add(Column.aliased("delete_at", table, columnPrefix + "_delete_at"));
        columns.add(Column.aliased("delete_by", table, columnPrefix + "_delete_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));

        columns.add(Column.aliased("uniform_id", table, columnPrefix + "_uniform_id"));
        columns.add(Column.aliased("uniform_release_id", table, columnPrefix + "_uniform_release_id"));
        columns.add(Column.aliased("uniform_order_id", table, columnPrefix + "_uniform_order_id"));
        columns.add(Column.aliased("uniform_return_id", table, columnPrefix + "_uniform_return_id"));
        columns.add(Column.aliased("uniform_order_stock_id", table, columnPrefix + "_uniform_order_stock_id"));
        columns.add(Column.aliased("actual_price", table, columnPrefix + "_actual_price"));
        columns.add(Column.aliased("uom_id", table, columnPrefix + "_uom_id"));
        columns.add(Column.aliased("uom_name", table, columnPrefix + "_uom_name"));
        return columns;
    }
}
