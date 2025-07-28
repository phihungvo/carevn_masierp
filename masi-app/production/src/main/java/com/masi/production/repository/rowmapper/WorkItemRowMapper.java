package com.masi.production.repository.rowmapper;

import com.masi.production.domain.WorkItem;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link WorkItem}, with proper type conversions.
 */
@Service
public class WorkItemRowMapper implements BiFunction<Row, String, WorkItem> {

    private final ColumnConverter converter;

    public WorkItemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link WorkItem} stored in the database.
     */
    @Override
    public WorkItem apply(Row row, String prefix) {
        WorkItem entity = new WorkItem();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        return entity;
    }
}
