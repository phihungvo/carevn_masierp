package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.Contract;
import com.masi.sale.domain.enumeration.ContractStatus;
import com.masi.sale.domain.enumeration.ContractType;
import io.r2dbc.spi.Row;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.data.relational.core.mapping.Column;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Contract}, with proper type conversions.
 */
@Service
public class ContractRowMapper implements BiFunction<Row, String, Contract> {

    private final ColumnConverter converter;

    public ContractRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link Contract} stored in the database.
     */
    @Override
    public Contract apply(Row row, String prefix) {
        Contract entity = new Contract();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ContractStatus.class));
        entity.setContractName(converter.fromRow(row, prefix + "_contract_name", String.class));
        entity.setContractValidFrom(converter.fromRow(row, prefix + "_contract_valid_from", LocalDate.class));
        entity.setContractValidTo(converter.fromRow(row, prefix + "_contract_valid_to", LocalDate.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", UUID.class));
        entity.setContractType(converter.fromRow(row, prefix + "_contract_type", ContractType.class));
        entity.setContractOwner(converter.fromRow(row, prefix + "_contract_owner", UUID.class));
        entity.setContractTotal(converter.fromRow(row, prefix + "_contract_total", BigDecimal.class));
        entity.setProteinPercent(converter.fromRow(row, prefix + "_protein_percent", String.class));
        entity.setApprovalSignFile(converter.fromRow(row, prefix + "_approval_sign_file", String.class));
        entity.setRejectNote(converter.fromRow(row, prefix + "_reject_note", String.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setDeliveryTermFrom(converter.fromRow(row, prefix + "_delivery_term_from", LocalDate.class));
        entity.setDeliveryTermTo(converter.fromRow(row, prefix + "_delivery_term_to", LocalDate.class));
        entity.setPayTerm(converter.fromRow(row, prefix + "_pay_term", String.class)); // Assuming payTerm is a float
        entity.setPayCondition(converter.fromRow(row, prefix + "_pay_condition", String.class));
        entity.setDeliveryLocation(converter.fromRow(row, prefix + "_delivery_location", String.class));

        entity.setMonetaryUnit(converter.fromRow(row, prefix + "_monetary_unit", String.class));
        entity.setExchangeRate(converter.fromRow(row, prefix + "_exchange_rate", Float.class));
        entity.setApprovalSignFileName(converter.fromRow(row, prefix + "_approval_sign_name", String.class));
        entity.setReviewAt(converter.fromRow(row, prefix + "_review_at", ZonedDateTime.class));
        entity.setReviewBy(converter.fromRow(row, prefix + "_review_by", UUID.class));
        entity.setQuotationId(converter.fromRow(row, prefix + "_quotation_id", UUID.class));
        entity.setOldStatus(converter.fromRow(row, prefix + "_old_status", ContractStatus.class));
        return entity;
    }
}
