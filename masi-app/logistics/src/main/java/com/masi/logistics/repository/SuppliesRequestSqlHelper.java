package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SuppliesRequestSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("request_number", table, columnPrefix + "_request_number"));
        columns.add(Column.aliased("request_date", table, columnPrefix + "_request_date"));
        columns.add(Column.aliased("request_by_employee_id", table, columnPrefix + "_request_by_employee_id"));
        columns.add(Column.aliased("department_id", table, columnPrefix + "_department_id"));
        columns.add(Column.aliased("request_status", table, columnPrefix + "_request_status"));
        columns.add(Column.aliased("total_amount", table, columnPrefix + "_total_amount"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("vat", table, columnPrefix + "_vat"));
        columns.add(Column.aliased("total_amount_after_vat", table, columnPrefix + "_total_amount_after_vat"));

        // Thêm các cột mới
        columns.add(Column.aliased("created_by_employee_id", table, columnPrefix + "_created_by_employee_id"));
        columns.add(Column.aliased("created_date_by_employee", table, columnPrefix + "_created_date_by_employee"));
        columns.add(Column.aliased("context", table, columnPrefix + "_context"));
        columns.add(Column.aliased("issue_date", table, columnPrefix + "_issue_date"));
        columns.add(Column.aliased("contract_id", table, columnPrefix + "_contract_id"));
        columns.add(Column.aliased("contract_code", table, columnPrefix + "_contract_code"));
        columns.add(Column.aliased("contract_content", table, columnPrefix + "_contract_content"));
        columns.add(Column.aliased("contract_date", table, columnPrefix + "_contract_date"));
        columns.add(Column.aliased("supplier_id", table, columnPrefix + "_supplier_id"));
        columns.add(Column.aliased("tax_number", table, columnPrefix + "_tax_number"));
        columns.add(Column.aliased("tel", table, columnPrefix + "_tel"));
        columns.add(Column.aliased("payment_method", table, columnPrefix + "_payment_method"));
        columns.add(Column.aliased("currency", table, columnPrefix + "_currency"));
        columns.add(Column.aliased("exchange_rate", table, columnPrefix + "_exchange_rate"));
        columns.add(Column.aliased("warehouse_id", table, columnPrefix + "_warehouse_id"));
        columns.add(Column.aliased("attached_files", table, columnPrefix + "_attached_files"));

        columns.add(Column.aliased("is_review", table, columnPrefix + "_is_review"));
        columns.add(Column.aliased("delivered_quantity", table, columnPrefix + "_delivered_quantity"));
        columns.add(Column.aliased("remaining_quantity", table, columnPrefix + "_remaining_quantity"));
        columns.add(Column.aliased("request_type_id", table, columnPrefix + "_request_type_id"));
        columns.add(Column.aliased("total_quantity", table, columnPrefix + "_total_quantity"));
        columns.add(Column.aliased("supplier_full_name", table, columnPrefix + "_supplier_full_name"));
        columns.add(Column.aliased("supplier_phone", table, columnPrefix + "_supplier_phone"));
        columns.add(Column.aliased("supplier_email", table, columnPrefix + "_supplier_email"));
        columns.add(Column.aliased("supplier_position", table, columnPrefix + "_supplier_position"));
        return columns;
    }
}
