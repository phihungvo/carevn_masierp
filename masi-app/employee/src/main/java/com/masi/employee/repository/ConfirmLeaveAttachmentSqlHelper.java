package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ConfirmLeaveAttachmentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("file_id", table, columnPrefix + "_file_id"));

        columns.add(Column.aliased("confirm_leave_id", table, columnPrefix + "_confirm_leave_id"));
        return columns;
    }
}
