package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.Order;
import com.masi.sale.domain.enumeration.OrderStatus;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.data.relational.core.sql.Column;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Order}, with proper type conversions.
 */
@Service
public class OrderRowMapper implements BiFunction<Row, String, Order> {

    private final ColumnConverter converter;

    public OrderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Order} stored in the database.
     */
    @Override
    public Order apply(Row row, String prefix) {

        Order entity = new Order();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", OrderStatus.class));
        entity.setOrderCode(converter.fromRow(row, prefix + "_order_code", String.class));
        entity.setNumberOrder(converter.fromRow(row, prefix + "_number_order", Integer.class));
        entity.setDateOrder(converter.fromRow(row, prefix + "_date_order", LocalDate.class));
        entity.setContractId(converter.fromRow(row, prefix + "_contract_id", UUID.class));
        entity.setPackageType(converter.fromRow(row, prefix + "_package_type", String.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", String.class));
        entity.setProtein(converter.fromRow(row, prefix + "_protein", String.class));
        entity.setHumidity(converter.fromRow(row, prefix + "_humidity", String.class));
        entity.setAshing(converter.fromRow(row, prefix + "_ashing", String.class));
        entity.setFat(converter.fromRow(row, prefix + "_fat", String.class));
        entity.setSalt(converter.fromRow(row, prefix + "_salt", String.class));
        entity.setTvn(converter.fromRow(row, prefix + "_tvn", String.class));
        entity.setImpurities(converter.fromRow(row, prefix + "_impurities", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setFinishDate(converter.fromRow(row, prefix + "_finish_date", LocalDate.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
       entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", UUID.class));

       //////////////// 16=-09 //////////////
        entity.setDeliveryTermFrom(converter.fromRow(row, prefix + "_delivery_term_from", LocalDate.class));
        entity.setDeliveryTermTo(converter.fromRow(row, prefix + "_delivery_term_to", LocalDate.class));
        entity.setPayTerm(converter.fromRow(row, prefix + "_pay_term", String.class)); // Assuming payTerm is a float
        entity.setPayCondition(converter.fromRow(row, prefix + "_pay_condition", String.class));
        entity.setDeliveryLocation(converter.fromRow(row, prefix + "_delivery_location", String.class));

        entity.setMonetaryUnit(converter.fromRow(row, prefix + "_monetary_unit", String.class));
        entity.setExchangeRate(converter.fromRow(row, prefix + "_exchange_rate", Float.class));

        return entity;
    }
}
