package com.carevn.masi.repository;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

import java.util.ArrayList;
import java.util.List;

public class AuthoritySqlHelper {
    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("action", table, columnPrefix + "_action"));
        columns.add(Column.aliased("resource", table, columnPrefix + "_resource"));
        return columns;
    }
}
