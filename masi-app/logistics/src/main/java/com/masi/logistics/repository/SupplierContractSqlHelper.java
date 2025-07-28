package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SupplierContractSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("contract_code", table, columnPrefix + "_contract_code"));
        columns.add(Column.aliased("contract_name", table, columnPrefix + "_contract_name"));
        columns.add(Column.aliased("supplier_id", table, columnPrefix + "_supplier_id"));
        columns.add(Column.aliased("contract_date", table, columnPrefix + "_contract_date"));
        columns.add(Column.aliased("end_date", table, columnPrefix + "_end_date"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("attachments", table, columnPrefix + "_attachments"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));

        columns.add(Column.aliased("contract_amount", table, columnPrefix + "_contract_amount"));
        columns.add(Column.aliased("supplies_request_id", table, columnPrefix + "_supplies_request_id"));
        columns.add(Column.aliased("payment_term_number", table, columnPrefix + "_payment_term_number"));
        columns.add(Column.aliased("start_date", table, columnPrefix + "_start_date"));
        columns.add(Column.aliased("total_amount", table, columnPrefix + "_total_amount"));
        columns.add(Column.aliased("total_amount_after_vat", table, columnPrefix + "_total_amount_after_vat"));
        columns.add(Column.aliased("total_quantity", table, columnPrefix + "_total_quantity"));
        columns.add(Column.aliased("supplier_full_name", table, columnPrefix + "_supplier_full_name"));
        columns.add(Column.aliased("supplier_position", table, columnPrefix + "_supplier_position"));
        columns.add(Column.aliased("supplier_phone", table, columnPrefix + "_supplier_phone"));
        columns.add(Column.aliased("supplier_email", table, columnPrefix + "_supplier_email"));
        columns.add(Column.aliased("delivery_est_date", table, columnPrefix + "_delivery_est_date"));
        columns.add(Column.aliased("delivery_status", table, columnPrefix + "_delivery_status"));
        return columns;
    }
}
