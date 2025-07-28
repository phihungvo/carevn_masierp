package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RecruitmentReviewRequestSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("position", table, columnPrefix + "_position"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        columns.add(Column.aliased("request_id", table, columnPrefix + "_request_id"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("approval_sign_file", table, columnPrefix + "_approval_sign_file"));
        columns.add(Column.aliased("reject_note", table, columnPrefix + "_reject_note"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("result", table, columnPrefix + "_result"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));

        return columns;
    }
}
