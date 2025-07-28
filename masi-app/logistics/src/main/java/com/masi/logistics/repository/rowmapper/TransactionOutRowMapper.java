package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.TransactionOut;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TransactionOut}, with proper type conversions.
 */
@Service
public class TransactionOutRowMapper implements BiFunction<Row, String, TransactionOut> {

    private final ColumnConverter converter;

    public TransactionOutRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TransactionOut} stored in the database.
     */
    @Override
    public TransactionOut apply(Row row, String prefix) {
        TransactionOut entity = new TransactionOut();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setTransactionId(converter.fromRow(row, prefix + "_transaction_id", UUID.class));
        entity.setTransactionCode(converter.fromRow(row, prefix + "_transaction_code", String.class));
        entity.setUnitPrice(converter.fromRow(row, prefix + "_unit_price", BigDecimal.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setRemain(converter.fromRow(row, prefix + "_remain", Integer.class));
        entity.setNotes(converter.fromRow(row, prefix + "_notes", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setExpiredDate(converter.fromRow(row, prefix + "_expired_date", ZonedDateTime.class));
        entity.setManufactureDate(converter.fromRow(row, prefix + "_manufacture_date", ZonedDateTime.class));
        entity.setItemId(converter.fromRow(row, prefix + "_item_id", UUID.class));
        entity.setItemCode(converter.fromRow(row, prefix + "_item_code", String.class));
        entity.setWarehouseId(converter.fromRow(row, prefix + "_warehouse_id", UUID.class));
        entity.setWarehouseCode(converter.fromRow(row, prefix + "_warehouse_code", String.class));
        entity.setSupplierId(converter.fromRow(row, prefix + "_supplier_id", UUID.class));
        entity.setSupplierCode(converter.fromRow(row, prefix + "_supplier_code", String.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", UUID.class));
        entity.setCustomerCode(converter.fromRow(row, prefix + "_customer_code", String.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", UUID.class));
        entity.setOrderCode(converter.fromRow(row, prefix + "_order_code", String.class));
        entity.setManufactureId(converter.fromRow(row, prefix + "_manufacture_id", UUID.class));
        entity.setManufactureCode(converter.fromRow(row, prefix + "_manufacture_code", String.class));
        entity.setPackingId(converter.fromRow(row, prefix + "_packing_id", UUID.class));
        entity.setPackingCode(converter.fromRow(row, prefix + "_packing_code", String.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setCreateBy(converter.fromRow(row, prefix + "_create_by", String.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", ZonedDateTime.class));
        entity.setUpdateBy(converter.fromRow(row, prefix + "_update_by", String.class));
        entity.setDeleteAt(converter.fromRow(row, prefix + "_delete_at", ZonedDateTime.class));
        entity.setDeleteBy(converter.fromRow(row, prefix + "_delete_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        return entity;
    }
}
