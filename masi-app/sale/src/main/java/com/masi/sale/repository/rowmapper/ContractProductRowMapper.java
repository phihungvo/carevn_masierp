package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.ContractProduct;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ContractProduct}, with proper type conversions.
 */
@Service
public class ContractProductRowMapper implements BiFunction<Row, String, ContractProduct> {

    private final ColumnConverter converter;

    public ContractProductRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ContractProduct} stored in the database.
     */
    @Override
    public ContractProduct apply(Row row, String prefix) {
        ContractProduct entity = new ContractProduct();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setIdContract(converter.fromRow(row, prefix + "_id_contract", UUID.class));
        entity.setIdProduct(converter.fromRow(row, prefix + "_id_product", UUID.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", Double.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Double.class));
        entity.setUnit(converter.fromRow(row, prefix + "_unit", String.class));
        entity.setProteinParameters(converter.fromRow(row, prefix + "_protein_parameters", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
