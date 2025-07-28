package com.carevn.masi.repository.rowmapper;

import com.carevn.masi.domain.Group;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.function.BiFunction;

@Service
public class GroupRowMapper implements BiFunction<Row, String, Group> {
    private final ColumnConverter converter;

    public GroupRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    @Override
    public Group apply(Row row, String prefix) {
        Group entity = new Group();
        entity.setId(row.get(prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setWorkspaceId(converter.fromRow(row, prefix + "_workspace_id", String.class));
        entity.setCompanyId(converter.fromRow(row, prefix + "_company_id", String.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setActivated(Boolean.TRUE.equals(converter.fromRow(row, prefix + "_activated", Boolean.class)));
        entity.setNormalizedName(converter.fromRow(row, prefix + "_normalized_name", String.class));
        return entity;
    }
}
