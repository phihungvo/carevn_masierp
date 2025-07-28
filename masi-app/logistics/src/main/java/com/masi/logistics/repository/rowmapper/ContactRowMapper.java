package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.Contact;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Contact}, with proper type conversions.
 */
@Service
public class ContactRowMapper implements BiFunction<Row, String, Contact> {

    private final ColumnConverter converter;

    public ContactRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Contact} stored in the database.
     */
    @Override
    public Contact apply(Row row, String prefix) {
        Contact entity = new Contact();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", String.class));
        entity.setBirthDate(converter.fromRow(row, prefix + "_birth_date", LocalDate.class));
        entity.setPosition(converter.fromRow(row, prefix + "_position", String.class));
        entity.setContactInfo(converter.fromRow(row, prefix + "_contact_info", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setContactTypeId(converter.fromRow(row, prefix + "_contact_type_id", UUID.class));
        return entity;
    }
}
