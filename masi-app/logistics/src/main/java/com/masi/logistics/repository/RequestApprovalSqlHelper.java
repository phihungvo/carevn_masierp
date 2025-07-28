package com.masi.logistics.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RequestApprovalSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("index", table, columnPrefix + "_index"));
        columns.add(Column.aliased("document_id", table, columnPrefix + "_document_id"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        columns.add(Column.aliased("result", table, columnPrefix + "_result"));
        columns.add(Column.aliased("approved_sign", table, columnPrefix + "_approved_sign"));
        columns.add(Column.aliased("approved_sign_name", table, columnPrefix + "_approved_sign_name"));
        columns.add(Column.aliased("reject_note", table, columnPrefix + "_reject_note"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("group_request", table, columnPrefix + "_group_request"));
        return columns;
    }
}
