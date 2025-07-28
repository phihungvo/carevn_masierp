package com.masi.sale.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ContractSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("contract_name", table, columnPrefix + "_contract_name"));
        columns.add(Column.aliased("contract_valid_from", table, columnPrefix + "_contract_valid_from"));
        columns.add(Column.aliased("contract_valid_to", table, columnPrefix + "_contract_valid_to"));
        columns.add(Column.aliased("customer_id", table, columnPrefix + "_customer_id"));
        columns.add(Column.aliased("contract_type", table, columnPrefix + "_contract_type"));
        columns.add(Column.aliased("contract_owner", table, columnPrefix + "_contract_owner"));
        columns.add(Column.aliased("contract_total", table, columnPrefix + "_contract_total"));
        columns.add(Column.aliased("protein_percent", table, columnPrefix + "_protein_percent"));
        columns.add(Column.aliased("approval_sign_file", table, columnPrefix + "_approval_sign_file"));
        columns.add(Column.aliased("reject_note", table, columnPrefix + "_reject_note"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("delivery_term_from", table, columnPrefix + "_delivery_term_from"));
        columns.add(Column.aliased("delivery_term_to", table, columnPrefix + "_delivery_term_to"));
        columns.add(Column.aliased("pay_term", table, columnPrefix + "_pay_term"));
        columns.add(Column.aliased("pay_condition", table, columnPrefix + "_pay_condition"));
        columns.add(Column.aliased("delivery_location", table, columnPrefix + "_delivery_location"));

        columns.add(Column.aliased("monetary_unit", table, columnPrefix + "_monetary_unit"));
        columns.add(Column.aliased("exchange_rate", table, columnPrefix + "_exchange_rate"));
        columns.add(Column.aliased("old_status", table, columnPrefix + "_old_status"));


        columns.add(Column.aliased("approval_sign_name", table, columnPrefix + "_approval_sign_name"));
        columns.add(Column.aliased("review_at", table, columnPrefix + "_review_at"));
        columns.add(Column.aliased("review_by", table, columnPrefix + "_review_by"));
        columns.add(Column.aliased("quotation_id", table, columnPrefix + "_quotation_id"));
        return columns;
    }
}
