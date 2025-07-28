package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.InventoriesDetail;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link InventoriesDetail}, with proper type conversions.
 */
@Service
public class InventoriesDetailRowMapper implements BiFunction<Row, String, InventoriesDetail> {

    private final ColumnConverter converter;

    public InventoriesDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link InventoriesDetail} stored in the database.
     */
    @Override
    public InventoriesDetail apply(Row row, String prefix) {
        InventoriesDetail entity = new InventoriesDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setItemId(converter.fromRow(row, prefix + "_item_id", UUID.class));
        entity.setInventoriesId(converter.fromRow(row, prefix + "_inventories_id", UUID.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", BigDecimal.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        entity.setTotalPrice(converter.fromRow(row, prefix + "_total_price", BigDecimal.class));
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
        entity.setVatAmount(converter.fromRow(row, prefix + "_vat_amount", BigDecimal.class));
        entity.setVatRate(converter.fromRow(row, prefix + "_vat_rate", Integer.class));
        entity.setVatId(converter.fromRow(row, prefix + "_vat_id", UUID.class));


        entity.setRegisterDate(converter.fromRow(row, prefix + "_register_date", ZonedDateTime.class));
        entity.setDepreciationDate(converter.fromRow(row, prefix + "_depreciation_date", ZonedDateTime.class));
        entity.setDepartmentId(converter.fromRow(row, prefix + "_department_id", UUID.class));
        entity.setUsageMonth(converter.fromRow(row, prefix + "_usage_month", Integer.class));
        entity.setHolder(converter.fromRow(row, prefix + "_holder", String.class));
        entity.setDepreciationAllocation(converter.fromRow(row, prefix + "_depreciation_allocation", String.class));
        entity.setExpenseAccount(converter.fromRow(row, prefix + "_expense_account", String.class));
        entity.setCostElements(converter.fromRow(row, prefix + "_cost_elements", String.class));
        entity.setUnitPrice(converter.fromRow(row, prefix + "_unit_price", BigDecimal.class));
        return entity;
    }
}
