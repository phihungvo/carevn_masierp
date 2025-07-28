package com.masi.sale.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PurchaseReviewSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("approval_status_sign_file", table, columnPrefix + "_approval_status_sign_file"));
        columns.add(Column.aliased("approval_status_note", table, columnPrefix + "_approval_status_note"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));

        columns.add(Column.aliased("purchase_request_id", table, columnPrefix + "_purchase_request_id"));
        return columns;
    }
}
