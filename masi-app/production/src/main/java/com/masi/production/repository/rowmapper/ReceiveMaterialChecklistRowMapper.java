package com.masi.production.repository.rowmapper;

import com.masi.production.domain.ReceiveMaterialChecklist;
import com.masi.production.domain.enumeration.MaterialType;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ReceiveMaterialChecklist}, with proper type conversions.
 */
@Service
public class ReceiveMaterialChecklistRowMapper implements BiFunction<Row, String, ReceiveMaterialChecklist> {

    private final ColumnConverter converter;

    public ReceiveMaterialChecklistRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReceiveMaterialChecklist} stored in the database.
     */
    @Override
    public ReceiveMaterialChecklist apply(Row row, String prefix) {
        ReceiveMaterialChecklist entity = new ReceiveMaterialChecklist();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCheckDate(converter.fromRow(row, prefix + "_check_date", LocalDate.class));
        entity.setCheckTime(converter.fromRow(row, prefix + "_check_time", ZonedDateTime.class));
        entity.setWeightNumber(converter.fromRow(row, prefix + "_weight_number", String.class));
        entity.setTransportCondition(converter.fromRow(row, prefix + "_transport_condition", Boolean.class));
        entity.setTransportNote(converter.fromRow(row, prefix + "_transport_note", String.class));
        entity.setCheckStatus(converter.fromRow(row, prefix + "_check_status", Boolean.class));
        entity.setStatusNote(converter.fromRow(row, prefix + "_status_note", String.class));
        entity.setCheckSmell(converter.fromRow(row, prefix + "_check_smell", Boolean.class));
        entity.setSmellNote(converter.fromRow(row, prefix + "_smell_note", String.class));
        entity.setCheckImpurity(converter.fromRow(row, prefix + "_check_impurity", Boolean.class));
        entity.setImpurityNote(converter.fromRow(row, prefix + "_impurity_note", String.class));
        entity.setCheckPoison(converter.fromRow(row, prefix + "_check_poison", Boolean.class));
        entity.setPoisonNote(converter.fromRow(row, prefix + "_poison_note", String.class));
        entity.setReceiverId(converter.fromRow(row, prefix + "_receiver_id", UUID.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setWorkItemId(converter.fromRow(row, prefix + "_work_item_id", UUID.class));
        entity.setMaterialType(converter.fromRow(row, prefix + "_material_type", MaterialType.class));
        entity.setWeight(converter.fromRow(row, prefix + "_weight", String.class));
        return entity;
    }
}
