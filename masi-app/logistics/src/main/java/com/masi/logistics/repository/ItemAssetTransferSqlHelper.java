package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ItemAssetTransferSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("attribute", table, columnPrefix + "_attribute"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("inventories_storage_id", table, columnPrefix + "_inventories_storage_id"));
        columns.add(Column.aliased("transaction_type_id", table, columnPrefix + "_transaction_type_id"));
        columns.add(Column.aliased("item_category_id", table, columnPrefix + "_item_category_id"));
        columns.add(Column.aliased("transfer_date", table, columnPrefix + "_transfer_date"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("from_unit", table, columnPrefix + "_from_unit"));
        columns.add(Column.aliased("from_department_id", table, columnPrefix + "_from_department_id"));
        columns.add(Column.aliased("to_department_id", table, columnPrefix + "_to_department_id"));
        columns.add(Column.aliased("from_person_id", table, columnPrefix + "_from_person_id"));
        columns.add(Column.aliased("to_person_id", table, columnPrefix + "_to_person_id"));
        columns.add(Column.aliased("from_address", table, columnPrefix + "_from_address"));
        columns.add(Column.aliased("to_address", table, columnPrefix + "_to_address"));
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
