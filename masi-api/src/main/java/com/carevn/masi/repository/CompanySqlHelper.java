package com.carevn.masi.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CompanySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("normalized_name", table, columnPrefix + "_normalized_name"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("parent_id", table, columnPrefix + "_parent_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("tax_code", table, columnPrefix + "_tax_code"));
        columns.add(Column.aliased("website", table, columnPrefix + "_website"));
        columns.add(Column.aliased("callcenter", table, columnPrefix + "_callcenter"));
        columns.add(Column.aliased("address", table, columnPrefix + "_address"));
        columns.add(Column.aliased("representative_name", table, columnPrefix + "_representative_name"));
        columns.add(Column.aliased("representative_phone", table, columnPrefix + "_representative_phone"));
        columns.add(Column.aliased("representative_email", table, columnPrefix + "_representative_email"));
        columns.add(Column.aliased("representative_dob", table, columnPrefix + "_representative_dob"));
        columns.add(Column.aliased("representative_id_number", table, columnPrefix + "_representative_id_number"));
        columns.add(Column.aliased("image_id", table, columnPrefix + "_image_id"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("is_activated", table, columnPrefix + "_is_activated"));

        return columns;
    }
}
