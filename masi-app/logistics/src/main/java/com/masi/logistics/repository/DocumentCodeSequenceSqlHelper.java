package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DocumentCodeSequenceSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("current_sequence", table, columnPrefix + "_current_sequence"));
        columns.add(Column.aliased("java_format", table, columnPrefix + "_java_format"));
        columns.add(Column.aliased("document_type", table, columnPrefix + "_document_type"));

        return columns;
    }
}
