package com.masi.production.repository.rowmapper;

import com.masi.production.domain.QualityCheckSample;
import com.masi.production.domain.enumeration.QcSampleStatus;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link QualityCheckSample}, with proper type conversions.
 */
@Service
public class QualityCheckSampleRowMapper implements BiFunction<Row, String, QualityCheckSample> {

    private final ColumnConverter converter;

    public QualityCheckSampleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link QualityCheckSample} stored in the database.
     */
    @Override
    public QualityCheckSample apply(Row row, String prefix) {
        QualityCheckSample entity = new QualityCheckSample();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setSamplingDate(converter.fromRow(row, prefix + "_sampling_date", LocalDate.class));
        entity.setSampleNo(converter.fromRow(row, prefix + "_sample_no", String.class));
        entity.setProductType(converter.fromRow(row, prefix + "_product_type", String.class));
        entity.setSampleWeight(converter.fromRow(row, prefix + "_sample_weight", Float.class));
        entity.setCustomer(converter.fromRow(row, prefix + "_customer", String.class));
        entity.setReason(converter.fromRow(row, prefix + "_reason", String.class));
        entity.setSampleReleaseDate(converter.fromRow(row, prefix + "_sample_release_date", LocalDate.class));
        entity.setInternalHum(converter.fromRow(row, prefix + "_internal_hum", String.class));
        entity.setInternalTvn(converter.fromRow(row, prefix + "_internal_tvn", String.class));
        entity.setInternalAsh(converter.fromRow(row, prefix + "_internal_ash", String.class));
        entity.setInternalProtein(converter.fromRow(row, prefix + "_internal_protein", String.class));
        entity.setExternalHum(converter.fromRow(row, prefix + "_external_hum", String.class));
        entity.setExternalTvn(converter.fromRow(row, prefix + "_external_tvn", String.class));
        entity.setExternalAsh(converter.fromRow(row, prefix + "_external_ash", String.class));
        entity.setExternalProtein(converter.fromRow(row, prefix + "_external_protein", String.class));
        entity.setSamplingEmployeeId(converter.fromRow(row, prefix + "_sampling_employee_id", UUID.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", QcSampleStatus.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setDisposalId(converter.fromRow(row, prefix + "_disposal_id", UUID.class));
        entity.setManufactureOrderId(converter.fromRow(row, prefix + "_manufacture_order_id", UUID.class));
        entity.setPackageId(converter.fromRow(row, prefix + "_package_id", UUID.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", UUID.class));
        entity.setProteinPercentageApply(converter.fromRow(row, prefix + "_protein_percentage", Float.class));
        entity.setAttributes(converter.fromRow(row, prefix + "_attributes", Json.class));
        entity.setItemId(converter.fromRow(row, prefix + "_item_id", UUID.class));
        entity.setProteinPercentageApply(converter.fromRow(row, prefix + "_protein_percentage_apply", Float.class));


        return entity;
    }
}
