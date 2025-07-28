package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.DeliveryDetail;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DeliveryDetail}, with proper type conversions.
 */
@Service
public class DeliveryDetailRowMapper implements BiFunction<Row, String, DeliveryDetail> {

    private final ColumnConverter converter;

    public DeliveryDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DeliveryDetail} stored in the database.
     */
    @Override
    public DeliveryDetail apply(Row row, String prefix) {
        DeliveryDetail entity = new DeliveryDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setDeliveryId(converter.fromRow(row, prefix + "_delivery_id", UUID.class));
        entity.setContractMaterialId(converter.fromRow(row, prefix + "_contract_material_id", UUID.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setUomId(converter.fromRow(row, prefix + "_uom_id", UUID.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setAttachment(converter.fromRow(row, prefix + "_attachment", Json.class));
        entity.setDeliveryDate(converter.fromRow(row, prefix + "_delivery_date", ZonedDateTime.class));
        entity.setActualQuantity(converter.fromRow(row, prefix + "_actual_quantity", Integer.class));
        entity.setDifferenceQuantity(converter.fromRow(row, prefix + "_difference_quantity", Integer.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", UUID.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setActualDeliveryDate(converter.fromRow(row, prefix + "_actual_delivery_date", ZonedDateTime.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));

        return entity;
    }
}
