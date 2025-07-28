package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DocumentarySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("document_number", table, columnPrefix + "_document_number"));
        columns.add(Column.aliased("date_start", table, columnPrefix + "_date_start"));
        columns.add(Column.aliased("documentary_group", table, columnPrefix + "_documentary_group"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("content", table, columnPrefix + "_content"));
        columns.add(Column.aliased("signer", table, columnPrefix + "_signer"));
        columns.add(Column.aliased("recipient", table, columnPrefix + "_recipient"));
        columns.add(Column.aliased("archive_location", table, columnPrefix + "_archive_location"));
        columns.add(Column.aliased("sender_or_receiver", table, columnPrefix + "_sender_or_receiver"));

        columns.add(Column.aliased("reject_note", table, columnPrefix + "_reject_note"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("deleted_at", table, columnPrefix + "_deleted_at"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));

        columns.add(Column.aliased("attachments_content_file", table, columnPrefix + "_attachments_content_file"));
        columns.add(Column.aliased("approval_sign_file", table, columnPrefix + "_approval_sign_file"));

        columns.add(Column.aliased("attachments", table, columnPrefix + "_attachments"));
        return columns;
    }
}
