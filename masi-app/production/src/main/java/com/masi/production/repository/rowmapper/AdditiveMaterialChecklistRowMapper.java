package com.masi.production.repository.rowmapper;

import com.masi.production.domain.AdditiveMaterialChecklist;
import com.masi.production.domain.enumeration.WeightUnit;
import io.r2dbc.spi.Row;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AdditiveMaterialChecklist}, with proper type conversions.
 */
@Service
public class AdditiveMaterialChecklistRowMapper implements BiFunction<Row, String, AdditiveMaterialChecklist> {

    private final ColumnConverter converter;

    public AdditiveMaterialChecklistRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link AdditiveMaterialChecklist} stored in the database.
     */
    @Override
    public AdditiveMaterialChecklist apply(Row row, String prefix) {
        AdditiveMaterialChecklist entity = new AdditiveMaterialChecklist();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCheckDate(converter.fromRow(row, prefix + "_check_date", LocalDate.class));
        entity.setCheckTime(converter.fromRow(row, prefix + "_check_time", ZonedDateTime.class));
        entity.setWeightNumber(converter.fromRow(row, prefix + "_weight_number", String.class));
        entity.setCheckImpurity(converter.fromRow(row, prefix + "_check_impurity", Boolean.class));
        entity.setImpurityNote(converter.fromRow(row, prefix + "_impurity_note", String.class));
        entity.setWeightMaterial(converter.fromRow(row, prefix + "_weight_material", String.class));
        entity.setWeightMaterialUnit(converter.fromRow(row, prefix + "_weight_material_unit", WeightUnit.class));
        entity.setBicabonatLotNumber(converter.fromRow(row, prefix + "_bicabonat_lot_number", String.class));
        entity.setBicacbonatWeight(converter.fromRow(row, prefix + "_bicacbonat_weight", Float.class));
        entity.setBicacbonatWeightUnit(converter.fromRow(row, prefix + "_bicacbonat_weight_unit", WeightUnit.class));
        entity.setReceiverId(converter.fromRow(row, prefix + "_receiver_id", UUID.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setWorkItemId(converter.fromRow(row, prefix + "_work_item_id", UUID.class));
        return entity;
    }
}
