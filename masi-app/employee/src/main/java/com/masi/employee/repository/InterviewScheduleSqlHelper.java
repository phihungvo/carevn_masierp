package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class InterviewScheduleSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("candidate_name", table, columnPrefix + "_candidate_name"));
        columns.add(Column.aliased("interview_date", table, columnPrefix + "_interview_date"));
        columns.add(Column.aliased("cv_file", table, columnPrefix + "_cv_file"));
        columns.add(Column.aliased("interviewer_id", table, columnPrefix + "_interviewer_id"));
        columns.add(Column.aliased("rate", table, columnPrefix + "_rate"));
        columns.add(Column.aliased("process", table, columnPrefix + "_process"));
        columns.add(Column.aliased("interview_mode", table, columnPrefix + "_interview_mode"));
        columns.add(Column.aliased("interview_result", table, columnPrefix + "_interview_result"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("phone_number", table, columnPrefix + "_phone_number"));
        columns.add(Column.aliased("recruitment_request_id", table, columnPrefix + "_recruitment_request_id"));
        return columns;
    }
}
