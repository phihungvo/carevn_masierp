package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.ProfileAttachment;
import com.masi.employee.domain.enumeration.ProfileAttachmentType;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ProfileAttachment}, with proper type conversions.
 */
@Service
public class ProfileAttachmentRowMapper implements BiFunction<Row, String, ProfileAttachment> {

    private final ColumnConverter converter;

    public ProfileAttachmentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProfileAttachment} stored in the database.
     */
    @Override
    public ProfileAttachment apply(Row row, String prefix) {
        ProfileAttachment entity = new ProfileAttachment();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setEmployeeProfileId(converter.fromRow(row, prefix + "_employee_profile_id", UUID.class));
        entity.setType(converter.fromRow(row, prefix + "_type", ProfileAttachmentType.class));
        entity.setPath(converter.fromRow(row, prefix + "_path", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
