package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.SupplierContract;
import com.masi.logistics.domain.enumeration.ContractStatus;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SupplierContract}, with proper type conversions.
 */
@Service
public class SupplierContractRowMapper implements BiFunction<Row, String, SupplierContract> {

    private final ColumnConverter converter;

    public SupplierContractRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SupplierContract} stored in the database.
     */
    @Override
    public SupplierContract apply(Row row, String prefix) {
        SupplierContract entity = new SupplierContract();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setContractCode(converter.fromRow(row, prefix + "_contract_code", String.class));
        entity.setContractName(converter.fromRow(row, prefix + "_contract_name", String.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));
        entity.setContractDate(converter.fromRow(row, prefix + "_contract_date", LocalDate.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", LocalDate.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setAttachments(converter.fromRow(row, prefix + "_attachments", Json.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ContractStatus.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));

        entity.setContractAmount(converter.fromRow(row, prefix + "_contract_amount", BigDecimal.class));
        entity.setSuppliesRequestId(converter.fromRow(row, prefix + "_supplies_request_id", UUID.class));
        entity.setPaymentTermNumber(converter.fromRow(row, prefix + "_payment_term_number", Double.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", LocalDate.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", BigDecimal.class));
        entity.setTotalAmountAfterVat(converter.fromRow(row, prefix + "_total_amount_after_vat", BigDecimal.class));
        entity.setTotalQuantity(converter.fromRow(row, prefix + "_total_quantity", BigDecimal.class));
        entity.setTotalAmountAfterVat(converter.fromRow(row, prefix + "_total_amount_after_vat", BigDecimal.class));
        entity.setTotalQuantity(converter.fromRow(row, prefix + "_total_quantity", BigDecimal.class));
        entity.setSupplierFullName(converter.fromRow(row, prefix + "_supplier_full_name", String.class));
        entity.setSupplierPosition(converter.fromRow(row, prefix + "_supplier_position", String.class));
        entity.setSupplierPhone(converter.fromRow(row, prefix + "_supplier_phone", String.class));
        entity.setSupplierEmail(converter.fromRow(row, prefix + "_supplier_email", String.class));
        entity.setDeliveryEstDate(converter.fromRow(row, prefix + "_delivery_est_date", LocalDate.class));
        entity.setDeliveryStatus(converter.fromRow(row, prefix + "_delivery_status", ContractStatus.DeliveryStatus.class));


        return entity;
    }
}
