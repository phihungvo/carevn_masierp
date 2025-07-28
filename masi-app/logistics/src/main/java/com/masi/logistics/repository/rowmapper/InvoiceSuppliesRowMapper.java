package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.InvoiceSupplies;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link InvoiceSupplies}, with proper type conversions.
 */
@Service
public class InvoiceSuppliesRowMapper implements BiFunction<Row, String, InvoiceSupplies> {

    private final ColumnConverter converter;

    public InvoiceSuppliesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link InvoiceSupplies} stored in the database.
     */
    @Override
    public InvoiceSupplies apply(Row row, String prefix) {
        InvoiceSupplies entity = new InvoiceSupplies();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setItemId(converter.fromRow(row, prefix + "_item_id", UUID.class));
        entity.setDetail1(converter.fromRow(row, prefix + "_detail_1", String.class));
        entity.setDetail2(converter.fromRow(row, prefix + "_detail_2", String.class));
        entity.setInvoiceId(converter.fromRow(row, prefix + "_invoice_id", UUID.class));
        entity.setSupplyId(converter.fromRow(row, prefix + "_supply_id", UUID.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", BigDecimal.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        entity.setTotal(converter.fromRow(row, prefix + "_total", BigDecimal.class));
        entity.setVat(converter.fromRow(row, prefix + "_vat", BigDecimal.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));

        entity.setVatAmount(converter.fromRow(row, prefix + "_vat_amount", BigDecimal.class));
        entity.setPreImportFee(converter.fromRow(row, prefix + "_pre_import_fee", BigDecimal.class));
        entity.setImportVatPercentage(converter.fromRow(row, prefix + "_import_vat_percentage", BigDecimal.class));
        entity.setImportVatAmount(converter.fromRow(row, prefix + "_import_vat_amount", BigDecimal.class));
        entity.setEnvFeePercentage(converter.fromRow(row, prefix + "_env_fee_percentage", BigDecimal.class));
        entity.setEnvFeeAmount(converter.fromRow(row, prefix + "_env_fee_amount", BigDecimal.class));
        entity.setPostImportFee(converter.fromRow(row, prefix + "_post_import_fee", BigDecimal.class));
        entity.setGrandTotal(converter.fromRow(row, prefix + "_grand_total", BigDecimal.class));
        entity.setVatId(converter.fromRow(row, prefix + "_vat_id", UUID.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setImportTaxPercentage(converter.fromRow(row, prefix + "_import_tax_percentage", BigDecimal.class));
        entity.setImportTaxAmount(converter.fromRow(row, prefix + "_import_tax_amount", BigDecimal.class));
        entity.setTotalAmountImportStock(converter.fromRow(row, prefix + "_total_amount_import_stock", BigDecimal.class));
        return entity;
    }
}
