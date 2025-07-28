package com.masi.utility.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class NotificationSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("content", table, columnPrefix + "_content"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("entity_name", table, columnPrefix + "_entity_name"));
        columns.add(Column.aliased("entity_id", table, columnPrefix + "_entity_id"));
        columns.add(Column.aliased("entity_type", table, columnPrefix + "_entity_type"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));

        columns.add(Column.aliased("data", table, columnPrefix + "_data"));
        columns.add(Column.aliased("category", table, columnPrefix + "_category"));
        columns.add(Column.aliased("sent_by", table, columnPrefix + "_sent_by"));
        return columns;
    }
}
