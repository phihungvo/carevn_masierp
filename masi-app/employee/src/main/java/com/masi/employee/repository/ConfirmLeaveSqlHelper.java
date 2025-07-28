package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ConfirmLeaveSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("submission_date", table, columnPrefix + "_submission_date"));
        columns.add(Column.aliased("reason", table, columnPrefix + "_reason"));
        columns.add(Column.aliased("recruitment_solution", table, columnPrefix + "_recruitment_solution"));
        columns.add(Column.aliased("hr_solution", table, columnPrefix + "_hr_solution"));
        columns.add(Column.aliased("leave_date", table, columnPrefix + "_leave_date"));

        return columns;
    }
}
