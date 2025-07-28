package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.ContractFile;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ContractFile}, with proper type conversions.
 */
@Service
public class ContractFileRowMapper implements BiFunction<Row, String, ContractFile> {

    private final ColumnConverter converter;

    public ContractFileRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ContractFile} stored in the database.
     */
    @Override
    public ContractFile apply(Row row, String prefix) {
        ContractFile entity = new ContractFile();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setContractFilePath(converter.fromRow(row, prefix + "_contract_file_path", String.class));
        entity.setContractIndexFilePath(converter.fromRow(row, prefix + "_contract_index_file_path", String.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setContractId(converter.fromRow(row, prefix + "_contract_id", UUID.class));
        return entity;
    }
}
