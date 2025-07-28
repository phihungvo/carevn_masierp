package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.UniformReturn;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UniformReturn}, with proper type conversions.
 */
@Service
public class UniformReturnRowMapper implements BiFunction<Row, String, UniformReturn> {

    private final ColumnConverter converter;

    public UniformReturnRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UniformReturn} stored in the database.
     */
    @Override
    public UniformReturn apply(Row row, String prefix) {
        UniformReturn entity = new UniformReturn();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", ZonedDateTime.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setUniformReleaseId(converter.fromRow(row, prefix + "_uniform_release_id", UUID.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        return entity;
    }
}
