package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.QuotationDetail;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link QuotationDetail}, with proper type
 * conversions.
 */
@Service
public class QuotationDetailRowMapper implements BiFunction<Row, String, QuotationDetail> {

    private final ColumnConverter converter;


    public QuotationDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link QuotationDetail} stored in the database.
     */
    @Override
    public QuotationDetail apply(Row row, String prefix) {
        QuotationDetail entity = new QuotationDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setDeliveryLocation(converter.fromRow(row, prefix + "_delivery_location", String.class));
        entity.setDeliveryDate(converter.fromRow(row, prefix + "_delivery_date", ZonedDateTime.class));
        entity.setPackaging(converter.fromRow(row, prefix + "_packaging", String.class));
        entity.setMinimumWeight(converter.fromRow(row, prefix + "_minimum_weight", String.class));
        entity.setWeight(converter.fromRow(row, prefix + "_weight", String.class));
        entity.setPriceType(converter.fromRow(row, prefix + "_price_type", String.class));
        entity.setPaymentMethod(converter.fromRow(row, prefix + "_payment_method", String.class));
        entity.setMaterialId(converter.fromRow(row, prefix + "_material_id", UUID.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setMaterialCriteria(converter.fromRow(row, prefix + "_material_criteria", String.class));

        entity.setQuotationId(converter.fromRow(row, prefix + "_quotation_id", UUID.class));
        entity.setNitrogen150Price(converter.fromRow(row, prefix + "_nitrogen_150_price", String.class));
        entity.setNitrogen180Price(converter.fromRow(row, prefix + "_nitrogen_180_price", String.class));

        entity.setDeliveryLocationEn(converter.fromRow(row, prefix + "_delivery_location_en", String.class));
        entity.setPackagingEn(converter.fromRow(row, prefix + "_packaging_en", String.class));
        entity.setPriceTypeEn(converter.fromRow(row, prefix + "_price_type_en", String.class));
        entity.setPaymentMethodEn(converter.fromRow(row, prefix + "_payment_method_en", String.class));
        entity.setMaterialCriteriaEn(converter.fromRow(row, prefix + "_material_criteria_en", String.class));
        entity.setIndex(converter.fromRow(row, prefix + "_index", Integer.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setDeletedDate(converter.fromRow(row, prefix + "_deleted_date", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", String.class));

        return entity;
    }
}
