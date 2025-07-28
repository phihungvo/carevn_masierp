package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.IncomingInvoice;
import com.masi.logistics.domain.enumeration.IncomingInvoiceStatus;
import com.masi.logistics.domain.enumeration.IncomingInvoiceType;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import jakarta.persistence.Column;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IncomingInvoice}, with proper type conversions.
 */
@Service
public class IncomingInvoiceRowMapper implements BiFunction<Row, String, IncomingInvoice> {

    private final ColumnConverter converter;

    public IncomingInvoiceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IncomingInvoice} stored in the database.
     */
    @Override
    public IncomingInvoice apply(Row row, String prefix) {
        IncomingInvoice entity = new IncomingInvoice();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setInvoiceNo(converter.fromRow(row, prefix + "_invoice_no", String.class));
        entity.setInvoiceDate(converter.fromRow(row, prefix + "_invoice_date", ZonedDateTime.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setDepartmentId(converter.fromRow(row, prefix + "_department_id", UUID.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", BigDecimal.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setAttachments(converter.fromRow(row, prefix + "_attachments", Json.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDocumentId(converter.fromRow(row, prefix + "_document_id", UUID.class));
        entity.setInvoiceType(converter.fromRow(row, prefix + "_invoice_type", IncomingInvoiceType.class));

        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));

        entity.setSeries(converter.fromRow(row, prefix + "_series", String.class));
        entity.setSupplierContractId(converter.fromRow(row, prefix + "_supplier_contract_id", UUID.class));
        entity.setInventoryInId(converter.fromRow(row, prefix + "_inventory_in_id", UUID.class));
        entity.setPaymentStatus(converter.fromRow(row, prefix + "_payment_status", String.class));
        entity.setPaymentMethod(converter.fromRow(row, prefix + "_payment_method", String.class));
        entity.setDebtDays(converter.fromRow(row, prefix + "_debt_days", Double.class));
        entity.setCurrencyId(converter.fromRow(row, prefix + "_currency_id", UUID.class));
        entity.setCurrencyRate(converter.fromRow(row, prefix + "_currency_rate", Double.class));
        entity.setTotalQuantity(converter.fromRow(row, prefix + "_total_quantity", BigDecimal.class));
        entity.setImportFee(converter.fromRow(row, prefix + "_import_fee", BigDecimal.class));
        //entity.setVat(converter.fromRow(row, prefix + "_vat", Double.class));
        entity.setTotalAmountVat(converter.fromRow(row, prefix + "_total_amount_vat", BigDecimal.class));
        entity.setGrandTotal(converter.fromRow(row, prefix + "_grand_total", BigDecimal.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", IncomingInvoiceStatus.class));
        entity.setCurrencyCode(converter.fromRow(row, prefix + "_currency_code", String.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));
        entity.setNeedApproval(converter.fromRow(row, prefix + "_need_approval", Boolean.class));
        entity.setPatternNo(converter.fromRow(row, prefix + "_pattern_no", String.class));
        entity.setReimbursementId(converter.fromRow(row, prefix + "_reimbursement_id", UUID.class));
        entity.setTotalAmountVat(converter.fromRow(row, prefix + "_total_amount_vat", BigDecimal.class));
        entity.setGrandTotal(converter.fromRow(row, prefix + "_grand_total", BigDecimal.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));
        entity.setNeedApproval(converter.fromRow(row, prefix + "_need_approval", Boolean.class));
        entity.setPatternNo(converter.fromRow(row, prefix + "_pattern_no", String.class));

        entity.setReimbursementId(converter.fromRow(row, prefix + "_reimbursement_id", UUID.class));

        entity.setTotalEnvTax(converter.fromRow(row, prefix + "_total_env_tax", BigDecimal.class));
        entity.setTotalImportTax(converter.fromRow(row, prefix + "_total_import_tax", BigDecimal.class));
        entity.setTotalVatPercentage(converter.fromRow(row, prefix + "_total_vat_percentage", Double.class));
        entity.setImportFee(converter.fromRow(row, prefix + "_import_fee", BigDecimal.class));
        entity.setTotalFeeAfterImport(converter.fromRow(row, prefix + "_total_fee_after_import", BigDecimal.class));
        entity.setTotalAmountSupplies(converter.fromRow(row, prefix + "_total_amount_supplies", BigDecimal.class));
        entity.setTotalPreImportFee(converter.fromRow(row, prefix + "_total_pre_import_fee", BigDecimal.class));
        entity.setTotalAmountAfterVat(converter.fromRow(row, prefix + "_total_amount_after_vat", BigDecimal.class));
        entity.setIsInvoice(converter.fromRow(row, prefix + "_is_invoice", Boolean.class));
        entity.setTotalAmountImportStock(converter.fromRow(row, prefix + "_total_amount_import_stock", BigDecimal.class));
        entity.setOrderCreatedAt(converter.fromRow(row, prefix + "_order_created_at", ZonedDateTime.class));

        return entity;
    }
}
