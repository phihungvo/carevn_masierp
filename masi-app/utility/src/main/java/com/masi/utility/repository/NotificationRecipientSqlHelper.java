package com.masi.utility.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class NotificationRecipientSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("recipient_id", table, columnPrefix + "_recipient_id"));
        columns.add(Column.aliased("read", table, columnPrefix + "_read"));
        columns.add(Column.aliased("read_at", table, columnPrefix + "_read_at"));

        columns.add(Column.aliased("notification_id", table, columnPrefix + "_notification_id"));
        return columns;
    }
}
