package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ItemAssetDepreciationDetailSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("attribute", table, columnPrefix + "_attribute"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("inventories_storage_id", table, columnPrefix + "_inventories_storage_id"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("cost_information", table, columnPrefix + "_cost_information"));
        columns.add(Column.aliased("amortized_cost_information", table, columnPrefix + "_amortized_cost_information"));
        columns.add(Column.aliased("amortization_amount", table, columnPrefix + "_amortization_amount"));
        columns.add(Column.aliased("amortization_rate", table, columnPrefix + "_amortization_rate"));
        columns.add(Column.aliased("accumulated_amortization_amount", table, columnPrefix + "_accumulated_amortization_amount"));
        columns.add(Column.aliased("recipe", table, columnPrefix + "_recipe"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("item_asset_depreciation_id", table, columnPrefix + "_item_asset_depreciation_id"));


        return columns;
    }
}
