package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.PaymentRequest;
import com.masi.logistics.domain.enumeration.RequestStatus;
import com.masi.logistics.domain.enumeration.RequestTypeEnum;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PaymentRequest}, with proper type conversions.
 */
@Service
public class PaymentRequestRowMapper implements BiFunction<Row, String, PaymentRequest> {

    private final ColumnConverter converter;

    public PaymentRequestRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PaymentRequest} stored in the database.
     */
    @Override
    public PaymentRequest apply(Row row, String prefix) {
        PaymentRequest entity = new PaymentRequest();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setReferenceNumber(converter.fromRow(row, prefix + "_reference_number", String.class));
        entity.setOrder(converter.fromRow(row, prefix + "_jhi_order", Integer.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", UUID.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setType(converter.fromRow(row, prefix + "_type", RequestTypeEnum.class));
        entity.setDepartmentId(converter.fromRow(row, prefix + "_department_id", UUID.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setAttachments(converter.fromRow(row, prefix + "_attachments", Json.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", BigDecimal.class));
        entity.setPaidAmount(converter.fromRow(row, prefix + "_paid_amount", BigDecimal.class));
        entity.setRemainingAmount(converter.fromRow(row, prefix + "_remaining_amount", BigDecimal.class));
        entity.setPaymentDate(converter.fromRow(row, prefix + "_payment_date", ZonedDateTime.class));
        entity.setReimbursementDate(converter.fromRow(row, prefix + "_reimbursement_date", ZonedDateTime.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", RequestStatus.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));

        entity.setPaymentVoucher(converter.fromRow(row, prefix + "_payment_voucher", String.class));
        entity.setPaymentVoucherAmount(converter.fromRow(row, prefix + "_payment_voucher_amount", BigDecimal.class));
        entity.setRemainingBalance(converter.fromRow(row, prefix + "_remaining_balance", BigDecimal.class));
        entity.setOverSpent(converter.fromRow(row, prefix + "_over_spent", BigDecimal.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", UUID.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));

        entity.setPaymentTermText(converter.fromRow(row, prefix + "_payment_term_text", String.class));

        return entity;
    }
}
