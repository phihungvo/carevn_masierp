package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SuppliesItemSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_supplies_request", table, columnPrefix + "_id_supplies_request"));
        columns.add(Column.aliased("id_item", table, columnPrefix + "_id_item"));
        columns.add(Column.aliased("id_uom", table, columnPrefix + "_id_uom"));
        columns.add(Column.aliased("supplies_id", table, columnPrefix + "_supplies_id"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("image_ids", table, columnPrefix + "_image_ids"));
        columns.add(Column.aliased("bank_info", table, columnPrefix + "_bank_info"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("vat", table, columnPrefix + "_vat"));
        columns.add(Column.aliased("total_amount_after_vat", table, columnPrefix + "_total_amount_after_vat"));
        columns.add(Column.aliased(("total_amount"), table, columnPrefix + "_total_amount"));
        columns.add(Column.aliased("vat_id", table, columnPrefix + "_vat_id"));
        return columns;
    }
}
