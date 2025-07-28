package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class InventoriesSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("receiver_user_id", table, columnPrefix + "_receiver_user_id"));
        columns.add(Column.aliased("delivery_date", table, columnPrefix + "_delivery_date"));
        columns.add(Column.aliased("inventories_type_id", table, columnPrefix + "_inventories_type_id"));
        columns.add(Column.aliased("date_create", table, columnPrefix + "_date_create"));
        columns.add(Column.aliased("customer_id", table, columnPrefix + "_customer_id"));
        columns.add(Column.aliased("customer_recipient_id", table, columnPrefix + "_customer_recipient_id"));
        columns.add(Column.aliased("invoice_id", table, columnPrefix + "_invoice_id"));
        columns.add(Column.aliased("address", table, columnPrefix + "_address"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("purchase_price", table, columnPrefix + "_purchase_price"));
        columns.add(Column.aliased("sale_price", table, columnPrefix + "_sale_price"));
        columns.add(Column.aliased("total_amount", table, columnPrefix + "_total_amount"));
        columns.add(Column.aliased("input_department_id", table, columnPrefix + "_input_department_id"));
        columns.add(Column.aliased("incoming_warehouse_id", table, columnPrefix + "_incoming_warehouse_id"));
        columns.add(Column.aliased("outgoing_warehouse_id", table, columnPrefix + "_outgoing_warehouse_id"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        columns.add(Column.aliased("order_id", table, columnPrefix + "_order_id"));
        columns.add(Column.aliased("supplier_request_id", table, columnPrefix + "_supplier_request_id"));
        columns.add(Column.aliased("tax_code", table, columnPrefix + "_tax_code"));
        columns.add(Column.aliased("series", table, columnPrefix + "_series"));
        columns.add(Column.aliased("currency_code_rate", table, columnPrefix + "_currency_code_rate"));
        columns.add(Column.aliased("exchange_rate", table, columnPrefix + "_exchange_rate"));
        columns.add(Column.aliased("is_emptiness", table, columnPrefix + "_is_emptiness"));
        columns.add(Column.aliased("emptiness_id", table, columnPrefix + "_emptiness_id"));
        columns.add(Column.aliased("file", table, columnPrefix + "_file"));
        columns.add(Column.aliased("attribute", table, columnPrefix + "_attribute"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));

        columns.add(Column.aliased("purchase_contract_id", table, columnPrefix + "_purchase_contract_id"));
        columns.add(Column.aliased("production_id", table, columnPrefix + "_production_id"));
        columns.add(Column.aliased("is_review", table, columnPrefix + "_is_review"));
        columns.add(Column.aliased("warehouse_type", table, columnPrefix + "_warehouse_type"));

        columns.add(Column.aliased("is_invoice", table, columnPrefix + "_is_invoice"));
        columns.add(Column.aliased("total_quantity", table, columnPrefix + "_total_quantity"));

        return columns;
    }
}
