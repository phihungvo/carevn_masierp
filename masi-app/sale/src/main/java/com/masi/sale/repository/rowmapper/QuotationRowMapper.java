package com.masi.sale.repository.rowmapper;

import com.masi.sale.domain.Quotation;
import com.masi.sale.domain.enumeration.QuotationStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Quotation}, with proper type conversions.
 */
@Service
public class QuotationRowMapper implements BiFunction<Row, String, Quotation> {

    private final ColumnConverter converter;

    public QuotationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Quotation} stored in the database.
     */
    @Override
    public Quotation apply(Row row, String prefix) {
        Quotation entity = new Quotation();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", QuotationStatus.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setFileId(converter.fromRow(row, prefix + "_file_id", String.class));
        entity.setFileName(converter.fromRow(row, prefix + "_file_name", String.class));
        entity.setRejectNote(converter.fromRow(row, prefix + "_reject_note", String.class));
        entity.setApproverId(converter.fromRow(row, prefix + "_approver_id", UUID.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setDeletedDate(converter.fromRow(row, prefix + "_deleted_date", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setCustomerRejectNote(converter.fromRow(row, prefix + "_customer_reject_note", String.class));
        entity.setCustomerApproverId(converter.fromRow(row, prefix + "_customer_approver_id", UUID.class));
        entity.setDeliveryDate(converter.fromRow(row, prefix + "_delivery_date", ZonedDateTime.class));
        entity.setDeliveryLocation(converter.fromRow(row, prefix + "_delivery_location", String.class));
        entity.setDeliveryLocationEn(converter.fromRow(row, prefix + "_delivery_location_en", String.class));
        entity.setPackagingEn(converter.fromRow(row, prefix + "_packaging_en", String.class));
        entity.setPackaging(converter.fromRow(row, prefix + "_packaging", String.class));
        entity.setMinimumWeight(converter.fromRow(row, prefix + "_minimum_weight", String.class));
        entity.setPaymentMethod(converter.fromRow(row, prefix + "_payment_method", String.class));
        entity.setPaymentMethodEn(converter.fromRow(row, prefix + "_payment_method_en", String.class));
        entity.setPriceType(converter.fromRow(row, prefix + "_price_type", String.class));
        entity.setPriceTypeEn(converter.fromRow(row, prefix + "_price_type_en", String.class));
        entity.setMaterialCriteria(converter.fromRow(row, prefix + "_material_criteria", String.class));
        entity.setMaterialCriteriaEn(converter.fromRow(row, prefix + "_material_criteria_en", String.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", UUID.class));
        entity.setProcessAt(converter.fromRow(row, prefix + "_process_at", ZonedDateTime.class));



        entity.setApprovalSignFile(converter.fromRow(row, prefix + "_approval_sign_file", String.class));
        entity.setApprovalSignName(converter.fromRow(row, prefix + "_approval_sign_name", String.class));

        return entity;
    }
}
