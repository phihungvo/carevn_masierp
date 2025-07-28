package com.masi.sale.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DeliveryDetailSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("delivery_id", table, columnPrefix + "_delivery_id"));
        columns.add(Column.aliased("contract_material_id", table, columnPrefix + "_contract_material_id"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("uom_id", table, columnPrefix + "_uom_id"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("attachment", table, columnPrefix + "_attachment"));
        columns.add(Column.aliased("delivery_date", table, columnPrefix + "_delivery_date"));
        columns.add(Column.aliased("order_id", table, columnPrefix + "_order_id"));
        columns.add(Column.aliased("actual_quantity", table, columnPrefix + "_actual_quantity"));
        columns.add(Column.aliased("difference_quantity", table, columnPrefix + "_difference_quantity"));
        columns.add(Column.aliased("address", table, columnPrefix + "_address"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("actual_delivery_date", table, columnPrefix + "_actual_delivery_date"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        return columns;
    }
}
