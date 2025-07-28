package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.DeliverySchedule;
import com.masi.sale.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DeliverySchedule}, with proper type conversions.
 */
@Service
public class DeliveryScheduleRowMapper implements BiFunction<Row, String, DeliverySchedule> {

    private final ColumnConverter converter;

    public DeliveryScheduleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DeliverySchedule} stored in the database.
     */
    @Override
    public DeliverySchedule apply(Row row, String prefix) {
        DeliverySchedule entity = new DeliverySchedule();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setDeliveryDate(converter.fromRow(row, prefix + "_delivery_date", LocalDate.class));
        entity.setExpectedReceiveDate(converter.fromRow(row, prefix + "_expected_receive_date", LocalDate.class));
        entity.setContractId(converter.fromRow(row, prefix + "_contract_id", UUID.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", UUID.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setUnitId(converter.fromRow(row, prefix + "_unit_id", UUID.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        entity.setTotal(converter.fromRow(row, prefix + "_total", BigDecimal.class));
        entity.setPaymentMethod(converter.fromRow(row, prefix + "_payment_method", String.class));
        entity.setReceiverName(converter.fromRow(row, prefix + "_receiver_name", String.class));
        entity.setDeliveryLocation(converter.fromRow(row, prefix + "_delivery_location", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setAttachment(converter.fromRow(row, prefix + "_attachment", Json.class));
        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", Json.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", StatusEntity.class));

        return entity;
    }
}
