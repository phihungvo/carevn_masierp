package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.Factories;
import com.masi.logistics.domain.enumeration.TypeFactory;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Factories}, with proper type conversions.
 */
@Service
public class FactoriesRowMapper implements BiFunction<Row, String, Factories> {

    private final ColumnConverter converter;

    public FactoriesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Factories} stored in the database.
     */
    @Override
    public Factories apply(Row row, String prefix) {
        Factories entity = new Factories();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setTypeFactory(converter.fromRow(row, prefix + "_type_factory", TypeFactory.class));
        entity.setDepartmentId(converter.fromRow(row, prefix + "_department_id", UUID.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCanDelete(converter.fromRow(row, prefix + "_can_delete", Boolean.class));
        entity.setNormalizedName(converter.fromRow(row, prefix + "_normalized_name", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setEmployeeOwnerId(converter.fromRow(row, prefix + "_employee_owner_id", UUID.class));
        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", Json.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        return entity;
    }
}
