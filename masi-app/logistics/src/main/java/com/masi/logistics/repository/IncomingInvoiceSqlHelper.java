package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class IncomingInvoiceSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("invoice_no", table, columnPrefix + "_invoice_no"));
        columns.add(Column.aliased("invoice_date", table, columnPrefix + "_invoice_date"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        columns.add(Column.aliased("department_id", table, columnPrefix + "_department_id"));
        columns.add(Column.aliased("content", table, columnPrefix + "_content"));
        columns.add(Column.aliased("total_amount", table, columnPrefix + "_total_amount"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("attachments", table, columnPrefix + "_attachments"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("document_id", table, columnPrefix + "_document_id"));
        columns.add(Column.aliased("invoice_type", table, columnPrefix + "_invoice_type"));

        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));

        columns.add(Column.aliased("series", table, columnPrefix + "_series"));
        columns.add(Column.aliased("supplier_contract_id", table, columnPrefix + "_supplier_contract_id"));
        columns.add(Column.aliased("inventory_in_id", table, columnPrefix + "_inventory_in_id"));
        columns.add(Column.aliased("payment_status", table, columnPrefix + "_payment_status"));
        columns.add(Column.aliased("payment_method", table, columnPrefix + "_payment_method"));
        columns.add(Column.aliased("debt_days", table, columnPrefix + "_debt_days"));
        columns.add(Column.aliased("currency_id", table, columnPrefix + "_currency_id"));
        columns.add(Column.aliased("currency_rate", table, columnPrefix + "_currency_rate"));
        columns.add(Column.aliased("total_quantity", table, columnPrefix + "_total_quantity"));
        columns.add(Column.aliased("import_fee", table, columnPrefix + "_import_fee"));
        columns.add(Column.aliased("vat", table, columnPrefix + "_vat"));
        columns.add(Column.aliased("vat_amount", table, columnPrefix + "_vat_amount"));
        columns.add(Column.aliased("total_amount_vat", table, columnPrefix + "_total_amount_vat"));
        columns.add(Column.aliased("grand_total", table, columnPrefix + "_grand_total"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("currency_code", table, columnPrefix + "_currency_code"));
        columns.add(Column.aliased("supplier_id", table, columnPrefix + "_supplier_id"));
        columns.add(Column.aliased("need_approval", table, columnPrefix + "_need_approval"));

        columns.add(Column.aliased("reimbursement_id", table, columnPrefix + "_reimbursement_id"));
        columns.add(Column.aliased("total_amount_vat", table, columnPrefix + "_total_amount_vat"));
        columns.add(Column.aliased("total_fee_after_import", table, columnPrefix + "_total_fee_after_import"));
        columns.add(Column.aliased("total_amount_supplies", table, columnPrefix + "_total_amount_supplies"));
        columns.add(Column.aliased("total_pre_import_fee", table, columnPrefix + "_total_pre_import_fee"));
        columns.add(Column.aliased("total_import_tax", table, columnPrefix + "_total_import_tax"));
        columns.add(Column.aliased("total_env_tax", table, columnPrefix + "_total_env_tax"));
        columns.add(Column.aliased("total_vat_percentage", table, columnPrefix + "_total_vat_percentage"));

        columns.add(Column.aliased("pattern_no", table, columnPrefix + "_pattern_no"));
        columns.add(Column.aliased("supplier_full_name", table, columnPrefix + "_supplier_full_name"));
        columns.add(Column.aliased("supplier_email", table, columnPrefix + "_supplier_email"));
        columns.add(Column.aliased("supplier_phone", table, columnPrefix + "_supplier_phone"));
        columns.add(Column.aliased("import_invoice_id", table, columnPrefix + "_import_invoice_id"));
        columns.add(Column.aliased("total_amount_after_vat", table, columnPrefix + "_total_amount_after_vat"));
        columns.add(Column.aliased("is_invoice", table, columnPrefix + "_is_invoice"));
        columns.add(Column.aliased("total_amount_import_stock", table, columnPrefix + "_total_amount_import_stock"));
        columns.add(Column.aliased("order_created_at", table, columnPrefix + "_order_created_at"));
        return columns;
    }
}
