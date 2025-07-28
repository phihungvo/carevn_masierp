package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RelatedCostsSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("invoice_id", table, columnPrefix + "_invoice_id"));
        columns.add(Column.aliased("payment_method_id", table, columnPrefix + "_payment_method_id"));
        columns.add(Column.aliased("payment_method_code", table, columnPrefix + "_payment_method_code"));
        columns.add(Column.aliased("payment_method_name", table, columnPrefix + "_payment_method_name"));
        columns.add(Column.aliased("vat_id", table, columnPrefix + "_vat_id"));
        columns.add(Column.aliased("vat", table, columnPrefix + "_vat"));
        columns.add(Column.aliased("vat_amount", table, columnPrefix + "_vat_amount"));
        columns.add(Column.aliased("total_amount", table, columnPrefix + "_total_amount"));
        columns.add(Column.aliased("debt_days", table, columnPrefix + "_debt_days"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));

        return columns;
    }
}
