package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class InvoiceSuppliesSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("item_id", table, columnPrefix + "_item_id"));
        columns.add(Column.aliased("detail_1", table, columnPrefix + "_detail_1"));
        columns.add(Column.aliased("detail_2", table, columnPrefix + "_detail_2"));
        columns.add(Column.aliased("invoice_id", table, columnPrefix + "_invoice_id"));
        columns.add(Column.aliased("supply_id", table, columnPrefix + "_supply_id"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));
        columns.add(Column.aliased("total", table, columnPrefix + "_total"));
        columns.add(Column.aliased("vat", table, columnPrefix + "_vat"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));

        columns.add(Column.aliased("vat_amount", table, columnPrefix + "_vat_amount"));
        columns.add(Column.aliased("pre_import_fee", table, columnPrefix + "_pre_import_fee"));
        columns.add(Column.aliased("import_vat_percentage", table, columnPrefix + "_import_vat_percentage"));
        columns.add(Column.aliased("import_vat_amount", table, columnPrefix + "_import_vat_amount"));
        columns.add(Column.aliased("env_fee_percentage", table, columnPrefix + "_env_fee_percentage"));
        columns.add(Column.aliased("env_fee_amount", table, columnPrefix + "_env_fee_amount"));
        columns.add(Column.aliased("post_import_fee", table, columnPrefix + "_post_import_fee"));
        columns.add(Column.aliased("grand_total", table, columnPrefix + "_grand_total"));
        columns.add(Column.aliased("vat_id", table, columnPrefix + "_vat_id"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("import_tax_percentage", table, columnPrefix + "_import_tax_percentage"));
        columns.add(Column.aliased("import_tax_amount", table, columnPrefix + "_import_tax_amount"));
        columns.add(Column.aliased("total_amount_import_stock", table, columnPrefix + "_total_amount_import_stock"));
        return columns;
    }
}
