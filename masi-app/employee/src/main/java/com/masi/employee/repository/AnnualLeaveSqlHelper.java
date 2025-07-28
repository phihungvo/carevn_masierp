package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AnnualLeaveSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("leave_after_probation", table, columnPrefix + "_leave_after_probation"));
        columns.add(Column.aliased("leave_per_year", table, columnPrefix + "_leave_per_year"));
        columns.add(Column.aliased("carry_forward_month", table, columnPrefix + "_carry_forward_month"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("work_place", table, columnPrefix + "_work_place"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));

        return columns;
    }
}
