package com.masi.production.repository.rowmapper;

import com.masi.production.domain.ManufactureOrder;
import com.masi.production.domain.enumeration.MoStatus;
import com.masi.production.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ManufactureOrder}, with proper type conversions.
 */
@Service
public class ManufactureOrderRowMapper implements BiFunction<Row, String, ManufactureOrder> {

    private final ColumnConverter converter;

    public ManufactureOrderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link ManufactureOrder} stored in the database.
     */
    @Override
    public ManufactureOrder apply(Row row, String prefix) {
        ManufactureOrder entity = new ManufactureOrder();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setFromDate(converter.fromRow(row, prefix + "_from_date", LocalDate.class));
        entity.setTypeProtein(converter.fromRow(row, prefix + "_type_protein", String.class));
        entity.setToDate(converter.fromRow(row, prefix + "_to_date", LocalDate.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", StatusEntity.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", UUID.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setQualityCheckSampleId(converter.fromRow(row, prefix + "_quality_check_sample_id", UUID.class));
        entity.setProductionStatus(converter.fromRow(row, prefix + "_production_status", String.class));
        entity.setProductionStandardId(converter.fromRow(row, prefix + "_production_standard_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setProductionQuantity(converter.fromRow(row, prefix + "_production_quantity", Float.class));

        entity.setMaterialId(converter.fromRow(row, prefix + "_material_id", UUID.class));
        entity.setPercentProtein(converter.fromRow(row, prefix + "_percent_protein", String.class));
        entity.setManufactureOrderType(converter.fromRow(row, prefix + "_manufacture_order_type", String.class));
        entity.setAttributes(converter.fromRow(row, prefix + "_attributes", Json.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", UUID.class));

        entity.setProductPackageId(converter.fromRow(row, prefix + "_product_package_id", UUID.class));
        entity.setProductMaintainId(converter.fromRow(row, prefix + "_product_maintain_id", UUID.class));

        entity.setProductionRoutingId(converter.fromRow(row, prefix + "_production_routing_id", UUID.class));
        entity.setInventoryId(converter.fromRow(row, prefix + "_inventory_id", UUID.class));

        entity.setOrderItemId(converter.fromRow(row, prefix + "_order_item_id", UUID.class));

        return entity;
    }
}
