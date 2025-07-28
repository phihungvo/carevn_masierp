package com.masi.production.repository.rowmapper;

import com.masi.production.domain.WorkOrder;
import com.masi.production.domain.enumeration.ManufactureOrderType;
import com.masi.production.domain.enumeration.WoStatus;
import com.masi.production.domain.enumeration.WorkOrderType;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link WorkOrder}, with proper type conversions.
 */
@Service
public class WorkOrderRowMapper implements BiFunction<Row, String, WorkOrder> {

    private final ColumnConverter converter;

    public WorkOrderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link WorkOrder} stored in the database.
     */
    @Override
    public WorkOrder apply(Row row, String prefix) {
        WorkOrder entity = new WorkOrder();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setFromDate(converter.fromRow(row, prefix + "_from_date", LocalDate.class));
        entity.setToDate(converter.fromRow(row, prefix + "_to_date", LocalDate.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", WoStatus.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setWorkItemId(converter.fromRow(row, prefix + "_work_item_id", UUID.class));
        entity.setManufactureOrderId(converter.fromRow(row, prefix + "_manufacture_order_id", UUID.class));
        entity.setChecklistType(converter.fromRow(row, prefix + "_checklist_type", WorkOrderType.class));
        entity.setManufactureOrderType(converter.fromRow(row, prefix + "_manufacture_order_type", ManufactureOrderType.class));
        return entity;
    }
}
