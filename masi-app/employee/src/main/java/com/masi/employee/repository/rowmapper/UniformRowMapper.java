package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.Uniform;
import io.r2dbc.spi.Row;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Uniform}, with proper type conversions.
 */
@Service
public class UniformRowMapper implements BiFunction<Row, String, Uniform> {

    private final ColumnConverter converter;

    public UniformRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link Uniform} stored in the database.
     */
    @Override
    public Uniform apply(Row row, String prefix) {
        Uniform entity = new Uniform();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setBasePrice(converter.fromRow(row, prefix + "_base_price", Double.class));
        entity.setUomGroupId(converter.fromRow(row, prefix + "_uom_group_id", UUID.class));
        entity.setUomId(converter.fromRow(row, prefix + "_uom_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", String.class));
        return entity;
    }
}
