package com.masi.production.repository.rowmapper;

import com.masi.production.domain.MixingReportChecklist;
import com.masi.production.domain.enumeration.WeightUnit;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

import javax.swing.border.EmptyBorder;

/**
 * Converter between {@link Row} to {@link MixingReportChecklist}, with proper type conversions.
 */
@Service
public class MixingReportChecklistRowMapper implements BiFunction<Row, String, MixingReportChecklist> {

    private final ColumnConverter converter;

    public MixingReportChecklistRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MixingReportChecklist} stored in the database.
     */
    @Override
    public MixingReportChecklist apply(Row row, String prefix) {
        MixingReportChecklist entity = new MixingReportChecklist();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCheckDate(converter.fromRow(row, prefix + "_check_date", LocalDate.class));
        entity.setFinProduct1No(converter.fromRow(row, prefix + "_fin_product_1_no", String.class));
        entity.setFinProduct1Weight(converter.fromRow(row, prefix + "_fin_product_1_weight", Float.class));
        entity.setFinProduct1WeightUnit(converter.fromRow(row, prefix + "_fin_product_1_weight_unit", WeightUnit.class));
        entity.setFinProduct2No(converter.fromRow(row, prefix + "_fin_product_2_no", String.class));
        entity.setFinProduct2Weight(converter.fromRow(row, prefix + "_fin_product_2_weight", Float.class));
        entity.setFinProduct2WeightUnit(converter.fromRow(row, prefix + "_fin_product_2_weight_unit", WeightUnit.class));
        entity.setBhtNo(converter.fromRow(row, prefix + "_bht_no", String.class));
        entity.setBhtWeight(converter.fromRow(row, prefix + "_bht_weight", Float.class));
        entity.setBhtWeightUnit(converter.fromRow(row, prefix + "_bht_weight_unit", WeightUnit.class));
        entity.setBhtWeightPrd(converter.fromRow(row, prefix + "_bht_weight_prd", Float.class));
        entity.setBhtWeightPrdUnit(converter.fromRow(row, prefix + "_bht_weight_prd_unit", WeightUnit.class));
        entity.setWeightPrdNo(converter.fromRow(row, prefix + "_weight_prd_no", String.class));
        entity.setCheckImpurity(converter.fromRow(row, prefix + "_check_impurity", Boolean.class));
        entity.setCheckImpurityNote(converter.fromRow(row, prefix + "_check_impurity_note", String.class));
        entity.setCheckSmell(converter.fromRow(row, prefix + "_check_smell", Boolean.class));
        entity.setCheckSmellNote(converter.fromRow(row, prefix + "_check_smell_note", String.class));
        entity.setCheckColor(converter.fromRow(row, prefix + "_check_color", Boolean.class));
        entity.setCheckColorNote(converter.fromRow(row, prefix + "_check_color_note", String.class));
        entity.setCheckEmployeeId(converter.fromRow(row, prefix + "_check_employee_id", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setMoisture(converter.fromRow(row, prefix + "_moisture", Float.class));
        entity.setTvn(converter.fromRow(row, prefix + "_tvn", Float.class));
        entity.setAsh(converter.fromRow(row, prefix + "_ash", Float.class));
        entity.setProtein(converter.fromRow(row, prefix + "_protein", Float.class));
        entity.setWorkItemId(converter.fromRow(row, prefix + "_work_item_id", UUID.class));
        return entity;
    }
}
