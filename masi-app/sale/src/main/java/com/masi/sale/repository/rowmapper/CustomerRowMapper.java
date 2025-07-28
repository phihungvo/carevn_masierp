package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.Customer;
import com.masi.sale.domain.enumeration.CustomerStatus;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Customer}, with proper type conversions.
 */
@Service
public class CustomerRowMapper implements BiFunction<Row, String, Customer> {

    private final ColumnConverter converter;

    public CustomerRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Customer} stored in the database.
     */
    @Override
    public Customer apply(Row row, String prefix) {
        Customer entity = new Customer();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCustomerCode(converter.fromRow(row, prefix + "_customer_code", String.class));
        entity.setCompanyName(converter.fromRow(row, prefix + "_company_name", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setTaxCode(converter.fromRow(row, prefix + "_tax_code", String.class));
        entity.setFirstName(converter.fromRow(row, prefix + "_first_name", String.class));
        entity.setLastName(converter.fromRow(row, prefix + "_last_name", String.class));
        entity.setBirthday(converter.fromRow(row, prefix + "_birthday", LocalDate.class));
        entity.setPhoneNumber(converter.fromRow(row, prefix + "_phone_number", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setPosition(converter.fromRow(row, prefix + "_position", String.class));
        entity.setCustomerOwner(converter.fromRow(row, prefix + "_customer_owner", UUID.class));
        entity.setContractSigned(converter.fromRow(row, prefix + "_contract_signed", LocalDate.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setCustomerStatus(converter.fromRow(row, prefix + "_customer_status", CustomerStatus.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));

        entity.setContractFrom(converter.fromRow(row, prefix + "_contract_from", LocalDate.class));
        entity.setContractTo(converter.fromRow(row, prefix + "_contract_to", LocalDate.class));
        return entity;
    }
}
