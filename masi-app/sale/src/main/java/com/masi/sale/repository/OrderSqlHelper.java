package com.masi.sale.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class OrderSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("order_code", table, columnPrefix + "_order_code"));
        columns.add(Column.aliased("number_order", table, columnPrefix + "_number_order"));
        columns.add(Column.aliased("date_order", table, columnPrefix + "_date_order"));
        columns.add(Column.aliased("contract_id", table, columnPrefix + "_contract_id"));
        columns.add(Column.aliased("package_type", table, columnPrefix + "_package_type"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("protein", table, columnPrefix + "_protein"));
        columns.add(Column.aliased("humidity", table, columnPrefix + "_humidity"));
        columns.add(Column.aliased("ashing", table, columnPrefix + "_ashing"));
        columns.add(Column.aliased("fat", table, columnPrefix + "_fat"));
        columns.add(Column.aliased("salt", table, columnPrefix + "_salt"));
        columns.add(Column.aliased("tvn", table, columnPrefix + "_tvn"));
        columns.add(Column.aliased("impurities", table, columnPrefix + "_impurities"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("finish_date", table, columnPrefix + "_finish_date"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        ////////////// 16-09 //////////////
        columns.add(Column.aliased("delivery_term_from", table, columnPrefix + "_delivery_term_from"));
        columns.add(Column.aliased("delivery_term_to", table, columnPrefix + "_delivery_term_to"));
        columns.add(Column.aliased("pay_term", table, columnPrefix + "_pay_term"));
        columns.add(Column.aliased("pay_condition", table, columnPrefix + "_pay_condition"));
        columns.add(Column.aliased("delivery_location", table, columnPrefix + "_delivery_location"));

        columns.add(Column.aliased("monetary_unit", table, columnPrefix + "_monetary_unit"));
        columns.add(Column.aliased("exchange_rate", table, columnPrefix + "_exchange_rate"));
        columns.add(Column.aliased("material_order_id", table, columnPrefix + "_material_order_id"));
        return columns;
    }
}
