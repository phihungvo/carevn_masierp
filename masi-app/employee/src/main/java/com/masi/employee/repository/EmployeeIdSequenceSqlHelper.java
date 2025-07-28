package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EmployeeIdSequenceSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("current_sequence", table, columnPrefix + "_current_sequence"));
        columns.add(Column.aliased("gender", table, columnPrefix + "_gender"));
        columns.add(Column.aliased("workspace_id", table, columnPrefix + "_workspace_id"));
        columns.add(Column.aliased("java_format", table, columnPrefix + "_java_format"));
        return columns;
    }
}
