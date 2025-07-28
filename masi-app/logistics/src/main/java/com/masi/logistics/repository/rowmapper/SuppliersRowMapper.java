package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.Suppliers;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Suppliers}, with proper type conversions.
 */
@Service
public class SuppliersRowMapper implements BiFunction<Row, String, Suppliers> {

    private final ColumnConverter converter;

    public SuppliersRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Suppliers} stored in the database.
     */
    @Override
    public Suppliers apply(Row row, String prefix) {
        Suppliers entity = new Suppliers();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setBankInfo(converter.fromRow(row, prefix + "_bank_info", String.class));
        entity.setTaxCode(converter.fromRow(row, prefix + "_tax_code", String.class));
        entity.setContact(converter.fromRow(row, prefix + "_contact", String.class));
        entity.setPaymentTerm(converter.fromRow(row, prefix + "_payment_term", ZonedDateTime.class));
        entity.setShortName(converter.fromRow(row, prefix + "_short_name", String.class));
        entity.setSupplierGroupId(converter.fromRow(row, prefix + "_supplier_group_id", UUID.class));
        entity.setFax(converter.fromRow(row, prefix + "_fax", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setAddressService(converter.fromRow(row, prefix + "_address_service", String.class));
        entity.setDebtEmployees(converter.fromRow(row, prefix + "_debt_employees", Json.class));
        var birthday = converter.fromRow(row, prefix + "_birthday", LocalDate.class);
        entity.setBirthday( birthday != null ? birthday.atStartOfDay(ZoneOffset.UTC): null);
        entity.setPaymentTermNumber(converter.fromRow(row, prefix + "_payment_term_number", Integer.class));
        entity.setManagerId(converter.fromRow(row, prefix + "_manager_id", UUID.class));
        entity.setPosition(converter.fromRow(row, prefix + "_position", String.class));
        entity.setFullName(converter.fromRow(row, prefix + "_full_name", String.class));
        entity.setAttachment(converter.fromRow(row, prefix + "_attachment", Json.class));
        entity.setPaymentTermText(converter.fromRow(row, prefix + "_payment_term_text", String.class));
        entity.setSupplierTypeId(converter.fromRow(row, prefix + "_supplier_type_id", UUID.class));
        return entity;
    }
}
