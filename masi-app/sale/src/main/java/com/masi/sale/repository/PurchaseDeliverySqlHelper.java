package com.masi.sale.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PurchaseDeliverySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("deliveried", table, columnPrefix + "_deliveried"));
        columns.add(Column.aliased("waiting_delivery", table, columnPrefix + "_waiting_delivery"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("updated_date", table, columnPrefix + "_updated_date"));

        columns.add(Column.aliased("purchase_request_id", table, columnPrefix + "_purchase_request_id"));
        return columns;
    }
}
