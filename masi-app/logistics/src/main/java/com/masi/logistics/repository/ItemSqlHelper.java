package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ItemSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("uom_id", table, columnPrefix + "_uom_id"));
        columns.add(Column.aliased("attribute", table, columnPrefix + "_attribute"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));

        columns.add(Column.aliased("item_category_id", table, columnPrefix + "_item_category_id"));
        columns.add(Column.aliased("percent_protein", table, columnPrefix + "_percent_protein"));
        columns.add(Column.aliased("notes", table, columnPrefix + "_notes"));
        columns.add(Column.aliased("vat_rate", table, columnPrefix + "_vat_rate"));
        columns.add(Column.aliased("unit_price", table, columnPrefix + "_unit_price"));
        columns.add(Column.aliased("revenue_group", table, columnPrefix + "_revenue_group"));
        columns.add(Column.aliased("supplier_id", table, columnPrefix + "_supplier_id"));
        columns.add(Column.aliased("item_type", table, columnPrefix + "_item_type"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));

        columns.add(Column.aliased("vat_id", table, columnPrefix + "_vat_id"));
        columns.add(Column.aliased("item_type_id", table, columnPrefix + "_item_type_id"));
        columns.add(Column.aliased("is_separation", table, columnPrefix + "_is_separation"));

        return columns;
    }
}
