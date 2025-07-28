package com.masi.production.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SampleDisposalSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("request_date", table, columnPrefix + "_request_date"));
        columns.add(Column.aliased("involve_employee", table, columnPrefix + "_involve_employee"));
        columns.add(Column.aliased("position", table, columnPrefix + "_position"));
        columns.add(Column.aliased("disposal_note", table, columnPrefix + "_disposal_note"));
        columns.add(Column.aliased("quantity_stt", table, columnPrefix + "_quantity_stt"));
        columns.add(Column.aliased("quantity_sample_name", table, columnPrefix + "_quantity_sample_name"));
        columns.add(Column.aliased("quantity_sample_no", table, columnPrefix + "_quantity_sample_no"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("quantity_save_date", table, columnPrefix + "_quantity_save_date"));
        columns.add(Column.aliased("quantity_release_date", table, columnPrefix + "_quantity_release_date"));
        columns.add(Column.aliased("disposal_method", table, columnPrefix + "_disposal_method"));
        columns.add(Column.aliased("disposal_result", table, columnPrefix + "_disposal_result"));
        columns.add(Column.aliased("reviewer_id", table, columnPrefix + "_reviewer_id"));
        columns.add(Column.aliased("requester_id", table, columnPrefix + "_requester_id"));
        columns.add(Column.aliased("reviewer_approved", table, columnPrefix + "_reviewer_approved"));
        columns.add(Column.aliased("reviewer_note", table, columnPrefix + "_reviewer_note"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("reviewer_sign_file", table, columnPrefix + "_reviewer_sign_file"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));

        return columns;
    }
}
