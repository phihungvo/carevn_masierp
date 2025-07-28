package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PaymentRequestSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("reference_number", table, columnPrefix + "_reference_number"));
        columns.add(Column.aliased("jhi_order", table, columnPrefix + "_jhi_order"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("department_id", table, columnPrefix + "_department_id"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("content", table, columnPrefix + "_content"));
        columns.add(Column.aliased("attachments", table, columnPrefix + "_attachments"));
        columns.add(Column.aliased("total_amount", table, columnPrefix + "_total_amount"));
        columns.add(Column.aliased("paid_amount", table, columnPrefix + "_paid_amount"));
        columns.add(Column.aliased("remaining_amount", table, columnPrefix + "_remaining_amount"));
        columns.add(Column.aliased("payment_date", table, columnPrefix + "_payment_date"));
        columns.add(Column.aliased("reimbursement_date", table, columnPrefix + "_reimbursement_date"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("supplier_id", table, columnPrefix + "_supplier_id"));
        columns.add(Column.aliased("payment_voucher", table, columnPrefix + "_payment_voucher"));
        columns.add(Column.aliased("payment_voucher_amount", table, columnPrefix + "_payment_voucher_amount"));
        columns.add(Column.aliased("remaining_balance", table, columnPrefix + "_remaining_balance"));
        columns.add(Column.aliased("over_spent", table, columnPrefix + "_over_spent"));

        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));

        columns.add(Column.aliased("payment_term_text", table, columnPrefix + "_payment_term_text"));

        return columns;
    }
}
