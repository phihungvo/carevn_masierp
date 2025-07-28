package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.RelatedCosts;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RelatedCosts}, with proper type conversions.
 */
@Service
public class RelatedCostsRowMapper implements BiFunction<Row, String, RelatedCosts> {

    private final ColumnConverter converter;

    public RelatedCostsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RelatedCosts} stored in the database.
     */
    @Override
    public RelatedCosts apply(Row row, String prefix) {
        RelatedCosts entity = new RelatedCosts();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setInvoiceId(converter.fromRow(row, prefix + "_invoice_id", UUID.class));
        entity.setPaymentMethodId(converter.fromRow(row, prefix + "_payment_method_id", UUID.class));
        entity.setPaymentMethodCode(converter.fromRow(row, prefix + "_payment_method_code", String.class));
        entity.setPaymentMethodName(converter.fromRow(row, prefix + "_payment_method_name", String.class));
        entity.setVatId(converter.fromRow(row, prefix + "_vat_id", UUID.class));
        entity.setVat(converter.fromRow(row, prefix + "_vat", Double.class));
        entity.setVatAmount(converter.fromRow(row, prefix + "_vat_amount", BigDecimal.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", BigDecimal.class));
        entity.setDebtDays(converter.fromRow(row, prefix + "_debt_days", Double.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        return entity;
    }
}
