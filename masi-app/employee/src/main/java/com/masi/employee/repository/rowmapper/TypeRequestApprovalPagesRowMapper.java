package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.TypeRequestApprovalPages;
import com.masi.employee.domain.enumeration.TypeRequestApproval;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TypeRequestApprovalPages}, with proper type conversions.
 */
@Service
public class TypeRequestApprovalPagesRowMapper implements BiFunction<Row, String, TypeRequestApprovalPages> {

    private final ColumnConverter converter;

    public TypeRequestApprovalPagesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TypeRequestApprovalPages} stored in the database.
     */
    @Override
    public TypeRequestApprovalPages apply(Row row, String prefix) {
        TypeRequestApprovalPages entity = new TypeRequestApprovalPages();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setPageName(converter.fromRow(row, prefix + "_page_name", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", TypeRequestApproval.class));
        entity.setNumberOfReviewers(converter.fromRow(row, prefix + "_number_of_reviewers", Integer.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));

        entity.setNote(converter.fromRow(row, prefix + "_note", Json.class));
        entity.setIsDepartment(converter.fromRow(row, prefix + "_is_department", Boolean.class));
        entity.setIsNominate(converter.fromRow(row, prefix + "_is_nominate", Boolean.class));
        return entity;
    }
}
