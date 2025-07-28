package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.ContactGift;
import com.masi.logistics.domain.enumeration.ContactGiftStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ContactGift}, with proper type conversions.
 */
@Service
public class ContactGiftRowMapper implements BiFunction<Row, String, ContactGift> {

    private final ColumnConverter converter;

    public ContactGiftRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ContactGift} stored in the database.
     */
    @Override
    public ContactGift apply(Row row, String prefix) {
        ContactGift entity = new ContactGift();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setGiftName(converter.fromRow(row, prefix + "_gift_name", String.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", Integer.class));
        entity.setIsGiving(converter.fromRow(row, prefix + "_is_giving", Boolean.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ContactGiftStatus.class));
        entity.setExpectedDate(converter.fromRow(row, prefix + "_expected_date", ZonedDateTime.class));
        entity.setDateOfGiving(converter.fromRow(row, prefix + "_date_of_giving", ZonedDateTime.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setContactId(converter.fromRow(row, prefix + "_contact_id", UUID.class));
        return entity;
    }
}
