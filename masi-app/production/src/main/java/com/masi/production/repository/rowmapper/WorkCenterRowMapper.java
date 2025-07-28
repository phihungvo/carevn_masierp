package com.masi.production.repository.rowmapper;

import com.masi.production.domain.WorkCenter;
import com.masi.production.domain.enumeration.WorkCenterStatusEnum;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link WorkCenter}, with proper type conversions.
 */
@Service
public class WorkCenterRowMapper implements BiFunction<Row, String, WorkCenter> {

    private final ColumnConverter converter;

    public WorkCenterRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link WorkCenter} stored in the database.
     */
    @Override
    public WorkCenter apply(Row row, String prefix) {
        WorkCenter entity = new WorkCenter();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", WorkCenterStatusEnum.class));
        entity.setCompanyId(converter.fromRow(row, prefix + "_company_id", UUID.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setLastCheckedAt(converter.fromRow(row, prefix + "_last_checked_at", ZonedDateTime.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        return entity;
    }
}
