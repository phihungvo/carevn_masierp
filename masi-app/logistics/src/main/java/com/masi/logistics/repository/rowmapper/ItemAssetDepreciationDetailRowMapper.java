package com.masi.logistics.repository.rowmapper;

import com.masi.logistics.domain.ItemAssetDepreciationDetail;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ItemAssetDepreciationDetail}, with proper type conversions.
 */
@Service
public class ItemAssetDepreciationDetailRowMapper implements BiFunction<Row, String, ItemAssetDepreciationDetail> {

    private final ColumnConverter converter;

    public ItemAssetDepreciationDetailRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ItemAssetDepreciationDetail} stored in the database.
     */
    @Override
    public ItemAssetDepreciationDetail apply(Row row, String prefix) {
        ItemAssetDepreciationDetail entity = new ItemAssetDepreciationDetail();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setInventoriesStorageId(converter.fromRow(row, prefix + "_inventories_storage_id", UUID.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setCostInformation(converter.fromRow(row, prefix + "_cost_information", String.class));
        entity.setAmortizedCostInformation(converter.fromRow(row, prefix + "_amortized_cost_information", String.class));
        entity.setAmortizationAmount(converter.fromRow(row, prefix + "_amortization_amount", BigDecimal.class));
        entity.setAmortizationRate(converter.fromRow(row, prefix + "_amortization_rate", BigDecimal.class));
        entity.setAccumulatedAmortizationAmount(converter.fromRow(row, prefix + "_accumulated_amortization_amount", BigDecimal.class));
        entity.setRecipe(converter.fromRow(row, prefix + "_recipe", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setItemAssetDepreciationId(converter.fromRow(row, prefix + "_item_asset_depreciation_id", UUID.class));


        return entity;
    }
}
