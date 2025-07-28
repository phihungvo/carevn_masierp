package com.masi.employee.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EmployeeProfileSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("employee_code", table, columnPrefix + "_employee_code"));
        columns.add(Column.aliased("full_name", table, columnPrefix + "_full_name"));
        columns.add(Column.aliased("gender", table, columnPrefix + "_gender"));
        columns.add(Column.aliased("workspace_id", table, columnPrefix + "_workspace_id"));
        columns.add(Column.aliased("citizen_id", table, columnPrefix + "_citizen_id"));
        columns.add(Column.aliased("citizen_issue_date", table, columnPrefix + "_citizen_issue_date"));
        columns.add(Column.aliased("citizen_issue_place", table, columnPrefix + "_citizen_issue_place"));
        columns.add(Column.aliased("residence_address", table, columnPrefix + "_residence_address"));
        columns.add(Column.aliased("temporary_address", table, columnPrefix + "_temporary_address"));
        columns.add(Column.aliased("birthday", table, columnPrefix + "_birthday"));
        columns.add(Column.aliased("phone", table, columnPrefix + "_phone"));
        columns.add(Column.aliased("tax_code", table, columnPrefix + "_tax_code"));
        columns.add(Column.aliased("start_work_date", table, columnPrefix + "_start_work_date"));
        columns.add(Column.aliased("role", table, columnPrefix + "_role"));
        columns.add(Column.aliased("position", table, columnPrefix + "_position"));
        columns.add(Column.aliased("bank_code", table, columnPrefix + "_bank_code"));
        columns.add(Column.aliased("bank_number", table, columnPrefix + "_bank_number"));
        columns.add(Column.aliased("contract_type", table, columnPrefix + "_contract_type"));
        columns.add(Column.aliased("contract_term", table, columnPrefix + "_contract_term"));
        columns.add(Column.aliased("contract_number", table, columnPrefix + "_contract_number"));
        columns.add(Column.aliased("contract_date", table, columnPrefix + "_contract_date"));
        columns.add(Column.aliased("contract_end_date", table, columnPrefix + "_contract_end_date"));
        columns.add(Column.aliased("level", table, columnPrefix + "_level"));
        columns.add(Column.aliased("parking_card", table, columnPrefix + "_parking_card"));
        columns.add(Column.aliased("insurance_card", table, columnPrefix + "_insurance_card"));
        columns.add(Column.aliased("referrer_id", table, columnPrefix + "_referrer_id"));
        columns.add(Column.aliased("referrer_date", table, columnPrefix + "_referrer_date"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("updated_at", table, columnPrefix + "_updated_at"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("department", table, columnPrefix + "_department"));


        columns.add(Column.aliased("probation_date_from", table, columnPrefix + "_probation_date_from"));
        columns.add(Column.aliased("probation_date_to", table, columnPrefix + "_probation_date_to"));
        columns.add(Column.aliased("official_work_type", table, columnPrefix + "_official_work_type"));
        columns.add(Column.aliased("official_work_type_duration", table, columnPrefix + "_official_work_type_duration"));
        columns.add(Column.aliased("insurance_payment_level", table, columnPrefix + "_insurance_payment_level"));
        columns.add(Column.aliased("pin", table, columnPrefix + "_pin"));
        columns.add(Column.aliased("account_status", table, columnPrefix + "_account_status"));
        return columns;
    }
}
