package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SuppliersSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("address", table, columnPrefix + "_address"));
        columns.add(Column.aliased("phone", table, columnPrefix + "_phone"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("create_at", table, columnPrefix + "_create_at"));
        columns.add(Column.aliased("create_by", table, columnPrefix + "_create_by"));
        columns.add(Column.aliased("update_at", table, columnPrefix + "_update_at"));
        columns.add(Column.aliased("update_by", table, columnPrefix + "_update_by"));
        columns.add(Column.aliased("delete_at", table, columnPrefix + "_delete_at"));
        columns.add(Column.aliased("delete_by", table, columnPrefix + "_delete_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("bank_info", table, columnPrefix + "_bank_info"));
        columns.add(Column.aliased("tax_code", table, columnPrefix + "_tax_code"));
        columns.add(Column.aliased("contact", table, columnPrefix + "_contact"));
        columns.add(Column.aliased("payment_term", table, columnPrefix + "_payment_term"));
        columns.add(Column.aliased("short_name", table, columnPrefix + "_short_name"));
        columns.add(Column.aliased("fax", table, columnPrefix + "_fax"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("address_service", table, columnPrefix + "_address_service"));

        columns.add(Column.aliased("supplier_group_id", table, columnPrefix + "_supplier_group_id"));
        columns.add(Column.aliased("debt_employees", table, columnPrefix + "_debt_employees"));
        columns.add(Column.aliased("birthday", table, columnPrefix + "_birthday"));
        columns.add(Column.aliased("payment_term_number", table, columnPrefix + "_payment_term_number"));
        columns.add(Column.aliased("manager_id", table, columnPrefix + "_manager_id"));
        columns.add(Column.aliased("position", table, columnPrefix + "_position"));
        columns.add(Column.aliased("full_name", table, columnPrefix + "_full_name"));
        columns.add(Column.aliased("attachment", table, columnPrefix + "_attachment"));
        columns.add(Column.aliased("payment_term_text", table, columnPrefix + "_payment_term_text"));
        columns.add(Column.aliased("supplier_type_id", table, columnPrefix + "_supplier_type_id"));
        return columns;
    }
}
