package com.masi.production.repository.rowmapper;

import com.masi.production.domain.MetalDetectionChecklist;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MetalDetectionChecklist}, with proper type conversions.
 */
@Service
public class MetalDetectionChecklistRowMapper implements BiFunction<Row, String, MetalDetectionChecklist> {

    private final ColumnConverter converter;

    public MetalDetectionChecklistRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MetalDetectionChecklist} stored in the database.
     */
    @Override
    public MetalDetectionChecklist apply(Row row, String prefix) {
        MetalDetectionChecklist entity = new MetalDetectionChecklist();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCheckDate(converter.fromRow(row, prefix + "_check_date", LocalDate.class));
        entity.setFinProductNo(converter.fromRow(row, prefix + "_fin_product_no", String.class));
        entity.setMagnetBegin(converter.fromRow(row, prefix + "_magnet_begin", Boolean.class));
        entity.setMagnetBeginNote(converter.fromRow(row, prefix + "_magnet_begin_note", String.class));
        entity.setMagnetEnd(converter.fromRow(row, prefix + "_magnet_end", Boolean.class));
        entity.setMagnetEndNote(converter.fromRow(row, prefix + "_magnet_end_note", String.class));
        entity.setScreen4Begin(converter.fromRow(row, prefix + "_screen_4_begin", Boolean.class));
        entity.setScreen4BeginNote(converter.fromRow(row, prefix + "_screen_4_begin_note", String.class));
        entity.setScreen4End(converter.fromRow(row, prefix + "_screen_4_end", Boolean.class));
        entity.setScreen4EndNote(converter.fromRow(row, prefix + "_screen_4_end_note", String.class));
        entity.setScreen3Begin(converter.fromRow(row, prefix + "_screen_3_begin", Boolean.class));
        entity.setScreen3BeginNote(converter.fromRow(row, prefix + "_screen_3_begin_note", String.class));
        entity.setScreen3End(converter.fromRow(row, prefix + "_screen_3_end", Boolean.class));
        entity.setScreen3EndNote(converter.fromRow(row, prefix + "_screen_3_end_note", String.class));
        entity.setCheckedBy(converter.fromRow(row, prefix + "_checked_by", UUID.class));
        entity.setAuditedBy(converter.fromRow(row, prefix + "_audited_by", UUID.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setWorkItemId(converter.fromRow(row, prefix + "_work_item_id", UUID.class));
        return entity;
    }
}
