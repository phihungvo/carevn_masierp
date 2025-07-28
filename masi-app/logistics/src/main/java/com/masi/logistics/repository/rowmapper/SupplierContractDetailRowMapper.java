package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.SupplierContractDetail;
import io.r2dbc.spi.Row;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SupplierContractDetail}, with proper type conversions.
 */
@Service
public class SupplierContractDetailRowMapper implements BiFunction<Row, String, SupplierContractDetail> {

    private final ColumnConverter converter;

    public SupplierContractDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SupplierContractDetail} stored in the database.
     */
    @Override
    public SupplierContractDetail apply(Row row, String prefix) {
        SupplierContractDetail entity = new SupplierContractDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setSupplyItemId(converter.fromRow(row, prefix + "_supply_item_id", UUID.class));
        entity.setUnitId(converter.fromRow(row, prefix + "_unit_id", UUID.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", BigDecimal.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setSupplierContractId(converter.fromRow(row, prefix + "_supplier_contract_id", UUID.class));

        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));

        entity.setVatAmount(converter.fromRow(row, prefix + "_vat_amount", BigDecimal.class));
        entity.setVatRate(converter.fromRow(row, prefix + "_vat_rate", Double.class));
        entity.setVatId(converter.fromRow(row, prefix + "_vat_id", UUID.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", BigDecimal.class));
        entity.setTotalAmountAfterVat(converter.fromRow(row, prefix + "_total_amount_after_vat", BigDecimal.class));
        return entity;
    }
}
