package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.Workspace;
import com.masi.employee.domain.enumeration.WorkspaceType;
import io.r2dbc.spi.Row;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Workspace}, with proper type conversions.
 */
@Service
public class WorkspaceRowMapper implements BiFunction<Row, String, Workspace> {

    private final ColumnConverter converter;

    public WorkspaceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link Workspace} stored in the database.
     */
    @Override
    public Workspace apply(Row row, String prefix) {
        Workspace entity = new Workspace();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setWorkspaceType(converter.fromRow(row, prefix + "_workspace_type", WorkspaceType.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setCanDelete(converter.fromRow(row, prefix + "_can_delete", Boolean.class));
        entity.setNormalizedName(converter.fromRow(row, prefix + "_normalized_name", String.class));
        return entity;
    }
}
