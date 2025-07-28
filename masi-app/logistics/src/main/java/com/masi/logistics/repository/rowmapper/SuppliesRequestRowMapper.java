package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.SuppliesRequest;
import com.masi.logistics.domain.enumeration.RequestStatus;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SuppliesRequest}, with proper type conversions.
 */
@Service
public class SuppliesRequestRowMapper implements BiFunction<Row, String, SuppliesRequest> {

    private final ColumnConverter converter;

    public SuppliesRequestRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SuppliesRequest} stored in the database.
     */
    @Override
    public SuppliesRequest apply(Row row, String prefix) {
        SuppliesRequest entity = new SuppliesRequest();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setRequestNumber(converter.fromRow(row, prefix + "_request_number", String.class));
        entity.setRequestDate(converter.fromRow(row, prefix + "_request_date", LocalDate.class));
        entity.setRequestByEmployeeId(converter.fromRow(row, prefix + "_request_by_employee_id", UUID.class));
        entity.setDepartmentId(converter.fromRow(row, prefix + "_department_id", UUID.class));
        entity.setRequestStatus(converter.fromRow(row, prefix + "_request_status", RequestStatus.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", BigDecimal.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setVat(converter.fromRow(row, prefix + "_vat", Double.class));
        entity.setTotalAmountAfterVat(converter.fromRow(row, prefix + "_total_amount_after_vat", BigDecimal.class));

        entity.setCreatedByEmployeeId(converter.fromRow(row, prefix + "_created_by_employee_id", UUID.class));
        entity.setCreatedDateByEmployee(converter.fromRow(row, prefix + "_created_date_by_employee", LocalDate.class));
        entity.setContext(converter.fromRow(row, prefix + "_context", String.class));
        entity.setIssueDate(converter.fromRow(row, prefix + "_issue_date", LocalDate.class));
        entity.setContractId(converter.fromRow(row, prefix + "_contract_id", String.class));
        entity.setContractCode(converter.fromRow(row, prefix + "_contract_code", String.class));
        entity.setContractContent(converter.fromRow(row, prefix + "_contract_content", String.class));
        entity.setContractDate(converter.fromRow(row, prefix + "_contract_date", LocalDate.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));
        entity.setTaxNumber(converter.fromRow(row, prefix + "_tax_number", String.class));
        entity.setTel(converter.fromRow(row, prefix + "_tel", String.class));
        entity.setPaymentMethod(converter.fromRow(row, prefix + "_payment_method", String.class));
        entity.setCurrency(converter.fromRow(row, prefix + "_currency", String.class));
        entity.setExchangeRate(converter.fromRow(row, prefix + "_exchange_rate", Float.class));
        entity.setWarehouseId(converter.fromRow(row, prefix + "_warehouse_id", String.class));
        entity.setAttachedFiles(converter.fromRow(row, prefix + "_attached_files", Json.class));

        entity.setIsReview(converter.fromRow(row, prefix + "_is_review", Boolean.class));
        entity.setDeliveredQuantity(converter.fromRow(row, prefix + "_delivered_quantity", BigDecimal.class));
        entity.setRemainingQuantity(converter.fromRow(row, prefix + "_remaining_quantity", BigDecimal.class));
        entity.setRequestTypeId(converter.fromRow(row, prefix + "_request_type_id", UUID.class));
        entity.setTotalQuantity(converter.fromRow(row, prefix + "_total_quantity", BigDecimal.class));

        entity.setSupplierFullName(converter.fromRow(row, prefix + "_supplier_full_name", String.class));
        entity.setSupplierPhone(converter.fromRow(row, prefix + "_supplier_phone", String.class));
        entity.setSupplierEmail(converter.fromRow(row, prefix + "_supplier_email", String.class));
        entity.setSupplierPosition(converter.fromRow(row, prefix + "_supplier_position", String.class));

        return entity;
    }

}
