package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.Inventories;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.domain.enumeration.WarehouseGroupType;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Inventories}, with proper type conversions.
 */
@Service
public class InventoriesRowMapper implements BiFunction<Row, String, Inventories> {

    private final ColumnConverter converter;

    public InventoriesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Inventories} stored in the database.
     */
    @Override
    public Inventories apply(Row row, String prefix) {
        Inventories entity = new Inventories();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setReceiverUserId(converter.fromRow(row, prefix + "_receiver_user_id", UUID.class));
        entity.setDeliveryDate(converter.fromRow(row, prefix + "_delivery_date", LocalDate.class));
        entity.setInventoriesTypeId(converter.fromRow(row, prefix + "_inventories_type_id", UUID.class));
        entity.setDateCreate(converter.fromRow(row, prefix + "_date_create", LocalDate.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", UUID.class));
        entity.setCustomerRecipientId(converter.fromRow(row, prefix + "_customer_recipient_id", UUID.class));
        entity.setInvoiceId(converter.fromRow(row, prefix + "_invoice_id", UUID.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setPurchasePrice(converter.fromRow(row, prefix + "_purchase_price", BigDecimal.class));
        entity.setSalePrice(converter.fromRow(row, prefix + "_sale_price", BigDecimal.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", BigDecimal.class));
        entity.setInputDepartmentId(converter.fromRow(row, prefix + "_input_department_id", UUID.class));
        entity.setIncomingWarehouseId(converter.fromRow(row, prefix + "_incoming_warehouse_id", UUID.class));
        entity.setOutgoingWarehouseId(converter.fromRow(row, prefix + "_outgoing_warehouse_id", UUID.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", UUID.class));
        entity.setSupplierRequestId(converter.fromRow(row, prefix + "_supplier_request_id", UUID.class));
        entity.setTaxCode(converter.fromRow(row, prefix + "_tax_code", String.class));
        entity.setSeries(converter.fromRow(row, prefix + "_series", String.class));
        entity.setCurrencyCodeRate(converter.fromRow(row, prefix + "_currency_code_rate", String.class));
        entity.setExchangeRate(converter.fromRow(row, prefix + "_exchange_rate", BigDecimal.class));
        entity.setIsEmptiness(converter.fromRow(row, prefix + "_is_emptiness", Boolean.class));
        entity.setEmptinessId(converter.fromRow(row, prefix + "_emptiness_id", UUID.class));
        entity.setFile(converter.fromRow(row, prefix + "_file", Json.class));
        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", Json.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", StatusEntity.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));

        entity.setPurchaseContractId(converter.fromRow(row, prefix + "_purchase_contract_id", UUID.class));
        entity.setProductionId(converter.fromRow(row, prefix + "_production_id", UUID.class));
        entity.setWarehouseGroupType(converter.fromRow(row, prefix + "_warehouse_type", WarehouseGroupType.class));
        entity.setIsReview(converter.fromRow(row, prefix + "_is_review", Boolean.class));

        entity.setTotalQuantity(converter.fromRow(row, prefix + "_total_quantity", BigDecimal.class));
        entity.setIsInvoice(converter.fromRow(row, prefix + "_is_invoice", Boolean.class));

        return entity;
    }
}
