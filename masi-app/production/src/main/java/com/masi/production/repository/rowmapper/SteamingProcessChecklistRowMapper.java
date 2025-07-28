package com.masi.production.repository.rowmapper;

import com.masi.production.domain.SteamingProcessChecklist;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SteamingProcessChecklist}, with proper type conversions.
 */
@Service
public class SteamingProcessChecklistRowMapper implements BiFunction<Row, String, SteamingProcessChecklist> {

    private final ColumnConverter converter;

    public SteamingProcessChecklistRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SteamingProcessChecklist} stored in the database.
     */
    @Override
    public SteamingProcessChecklist apply(Row row, String prefix) {
        SteamingProcessChecklist entity = new SteamingProcessChecklist();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCheckDate(converter.fromRow(row, prefix + "_check_date", LocalDate.class));
        entity.setCheckTime(converter.fromRow(row, prefix + "_check_time", ZonedDateTime.class));
        entity.setWeightNumber(converter.fromRow(row, prefix + "_weight_number", String.class));
        entity.setSteamerAtm(converter.fromRow(row, prefix + "_steamer_atm", String.class));
        entity.setSteamerTemp(converter.fromRow(row, prefix + "_steamer_temp", String.class));
        entity.setSteamerTime(converter.fromRow(row, prefix + "_steamer_time", ZonedDateTime.class));
        entity.setTub1Atm(converter.fromRow(row, prefix + "_tub_1_atm", String.class));
        entity.setTub1Temp(converter.fromRow(row, prefix + "_tub_1_temp", String.class));
        entity.setTub1Time(converter.fromRow(row, prefix + "_tub_1_time", ZonedDateTime.class));
        entity.setTub2Atm(converter.fromRow(row, prefix + "_tub_2_atm", String.class));
        entity.setTub2Temp(converter.fromRow(row, prefix + "_tub_2_temp", String.class));
        entity.setTub2Time(converter.fromRow(row, prefix + "_tub_2_time", ZonedDateTime.class));
        entity.setFinProductNo(converter.fromRow(row, prefix + "_fin_product_no", String.class));
        entity.setReceiverId(converter.fromRow(row, prefix + "_receiver_id", UUID.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setWorkItemId(converter.fromRow(row, prefix + "_work_item_id", UUID.class));
        return entity;
    }
}
