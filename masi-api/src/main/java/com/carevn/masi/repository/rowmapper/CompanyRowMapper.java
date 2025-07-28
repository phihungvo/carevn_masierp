package com.carevn.masi.repository.rowmapper;

import com.carevn.masi.domain.Company;
import io.r2dbc.spi.Row;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Company}, with proper type conversions.
 */
@Service
public class CompanyRowMapper implements BiFunction<Row, String, Company> {

    private final ColumnConverter converter;

    public CompanyRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Company} stored in the database.
     */
    @Override
    public Company apply(Row row, String prefix) {
        Company entity = new Company();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", UUID.class));
        entity.setNormalizedName(converter.fromRow(row, prefix + "_normalized_name", String.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setTaxCode(converter.fromRow(row, prefix + "_tax_code", String.class));
        entity.setWebsite(converter.fromRow(row, prefix + "_website", String.class));
        entity.setCallcenter(converter.fromRow(row, prefix + "_callcenter", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setRepresentativeName(converter.fromRow(row, prefix + "_representative_name", String.class));
        entity.setRepresentativePhone(converter.fromRow(row, prefix + "_representative_phone", String.class));
        entity.setRepresentativeEmail(converter.fromRow(row, prefix + "_representative_email", String.class));
        entity.setRepresentativeDob(converter.fromRow(row, prefix + "_representative_dob", LocalDate.class));
        entity.setRepresentativeIdNumber(converter.fromRow(row, prefix + "_representative_id_number", String.class));
        entity.setImageId(converter.fromRow(row, prefix + "_image_id", UUID.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setIsActivated(converter.fromRow(row, prefix + "_is_activated", Boolean.class));
        return entity;
    }
}
