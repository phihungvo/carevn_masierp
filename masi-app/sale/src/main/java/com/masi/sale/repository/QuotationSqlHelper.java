package com.masi.sale.repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class QuotationSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("file_id", table, columnPrefix + "_file_id"));
        columns.add(Column.aliased("file_name", table, columnPrefix + "_file_name"));
        columns.add(Column.aliased("reject_note", table, columnPrefix + "_reject_note"));
        columns.add(Column.aliased("approver_id", table, columnPrefix + "_approver_id"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("deleted_date", table, columnPrefix + "_deleted_date"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("customer_reject_note", table, columnPrefix + "_customer_reject_note"));
        columns.add(Column.aliased("customer_approver_id", table, columnPrefix + "_customer_approver_id"));
        columns.add(Column.aliased("delivery_date", table, columnPrefix + "_delivery_date"));
        columns.add(Column.aliased("delivery_location", table, columnPrefix + "_delivery_location"));
        columns.add(Column.aliased("delivery_location_en", table, columnPrefix + "_delivery_location_en"));
        columns.add(Column.aliased("packaging", table, columnPrefix + "_packaging"));
        columns.add(Column.aliased("packaging_en", table, columnPrefix + "_packaging_en"));
        columns.add(Column.aliased("minimum_weight", table, columnPrefix + "_minimum_weight"));
        columns.add(Column.aliased("payment_method", table, columnPrefix + "_payment_method"));
        columns.add(Column.aliased("payment_method_en", table, columnPrefix + "_payment_method_en"));
        columns.add(Column.aliased("price_type", table, columnPrefix + "_price_type"));
        columns.add(Column.aliased("price_type_en", table, columnPrefix + "_price_type_en"));
        columns.add(Column.aliased("material_criteria", table, columnPrefix + "_material_criteria"));
        columns.add(Column.aliased("material_criteria_en", table, columnPrefix + "_material_criteria_en"));
        columns.add(Column.aliased("customer_id", table, columnPrefix + "_customer_id"));

        columns.add(Column.aliased("approval_sign_file", table, columnPrefix + "_approval_sign_file"));
        columns.add(Column.aliased("approval_sign_name", table, columnPrefix + "_approval_sign_name"));
        columns.add(Column.aliased("process_at", table, columnPrefix + "_process_at"));
        return columns;
    }
}
