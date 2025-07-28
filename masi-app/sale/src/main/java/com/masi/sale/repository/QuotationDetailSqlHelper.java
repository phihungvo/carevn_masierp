package com.masi.sale.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class QuotationDetailSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("delivery_location", table, columnPrefix + "_delivery_location"));
        columns.add(Column.aliased("delivery_location_en", table, columnPrefix + "_delivery_location_en"));
        columns.add(Column.aliased("delivery_date", table, columnPrefix + "_delivery_date"));
        columns.add(Column.aliased("packaging", table, columnPrefix + "_packaging"));
        columns.add(Column.aliased("packaging_en", table, columnPrefix + "_packaging_en"));
        columns.add(Column.aliased("minimum_weight", table, columnPrefix + "_minimum_weight"));
        columns.add(Column.aliased("weight", table, columnPrefix + "_weight"));
        columns.add(Column.aliased("price_type", table, columnPrefix + "_price_type"));
        columns.add(Column.aliased("price_type_en", table, columnPrefix + "_price_type_en"));
        columns.add(Column.aliased("payment_method", table, columnPrefix + "_payment_method"));
        columns.add(Column.aliased("payment_method_en", table, columnPrefix + "_payment_method_en"));
        columns.add(Column.aliased("material_id", table, columnPrefix + "_material_id"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("material_criteria", table, columnPrefix + "_material_criteria"));
        columns.add(Column.aliased("material_criteria_en", table, columnPrefix + "_material_criteria_en"));
        columns.add(Column.aliased("index", table, columnPrefix + "_index"));
        columns.add(Column.aliased("quotation_id", table, columnPrefix + "_quotation_id"));
        columns.add(Column.aliased("nitrogen_150_price", table, columnPrefix + "_nitrogen_150_price"));
        columns.add(Column.aliased("nitrogen_180_price", table, columnPrefix + "_nitrogen_180_price"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));

        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("last_updated", table, columnPrefix + "_last_updated"));
        columns.add(Column.aliased("updated_by", table, columnPrefix + "_updated_by"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));
        columns.add(Column.aliased("deleted_date", table, columnPrefix + "_deleted_date"));
        columns.add(Column.aliased("deleted_by", table, columnPrefix + "_deleted_by"));
        return columns;
    }
}
