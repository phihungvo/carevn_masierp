package com.masi.employee.repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CallCenterSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("reception_date", table, columnPrefix + "_reception_date"));
        columns.add(Column.aliased("group_cs", table, columnPrefix + "_group_cs"));
        columns.add(Column.aliased("phone_of_caller", table, columnPrefix + "_phone_of_caller"));
        columns.add(Column.aliased("phone_of_name", table, columnPrefix + "_phone_of_name"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("type_cs", table, columnPrefix + "_type_cs"));
        columns.add(Column.aliased("customer_id", table, columnPrefix + "_customer_id"));
        columns.add(Column.aliased("employee_created_id", table, columnPrefix + "_employee_created_id"));
        columns.add(Column.aliased("attribute", table, columnPrefix + "_attribute"));
        columns.add(Column.aliased("problem_content", table, columnPrefix + "_problem_content"));
        columns.add(Column.aliased("resolution_content", table, columnPrefix + "_resolution_content"));
        columns.add(Column.aliased("response_content", table, columnPrefix + "_response_content"));
        columns.add(Column.aliased("employee_assign_id", table, columnPrefix + "_employee_assign_id"));
        columns.add(Column.aliased("employee_close_id", table, columnPrefix + "_employee_close_id"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("type_page_cs", table, columnPrefix + "_type_page_cs"));

        columns.add(Column.aliased("attachment", table, columnPrefix + "_attachment"));
        columns.add(Column.aliased("employee_assign_date", table, columnPrefix + "_employee_assign_date"));
        columns.add(Column.aliased("employee_close_date", table, columnPrefix + "_employee_close_date"));
        columns.add(Column.aliased("confirm_date", table, columnPrefix + "_confirm_date"));
        columns.add(Column.aliased("source_cs", table, columnPrefix + "_source_cs"));

        return columns;
    }
}
