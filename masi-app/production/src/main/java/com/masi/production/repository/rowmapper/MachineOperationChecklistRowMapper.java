package com.masi.production.repository.rowmapper;

import com.masi.production.domain.MachineOperationChecklist;
import io.r2dbc.spi.Row;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MachineOperationChecklist}, with proper type conversions.
 */
@Service
public class MachineOperationChecklistRowMapper implements BiFunction<Row, String, MachineOperationChecklist> {

    private final ColumnConverter converter;

    public MachineOperationChecklistRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MachineOperationChecklist} stored in the database.
     */
    @Override
    public MachineOperationChecklist apply(Row row, String prefix) {
        MachineOperationChecklist entity = new MachineOperationChecklist();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCheckTime(converter.fromRow(row, prefix + "_check_time", ZonedDateTime.class));
        entity.setIncineratorAirDuct(converter.fromRow(row, prefix + "_incinerator_air_duct", Boolean.class));
        entity.setIncineratorAirDuctNote(converter.fromRow(row, prefix + "_incinerator_air_duct_note", String.class));
        entity.setIncinerator(converter.fromRow(row, prefix + "_incinerator", Boolean.class));
        entity.setIncineratorCheckNote(converter.fromRow(row, prefix + "_incinerator_check_note", String.class));
        entity.setDryingOvenAirDuct(converter.fromRow(row, prefix + "_drying_oven_air_duct", Boolean.class));
        entity.setDryingOvenAirDuctNote(converter.fromRow(row, prefix + "_drying_oven_air_duct_note", String.class));
        entity.setDryingOvenMeter(converter.fromRow(row, prefix + "_drying_oven_meter", Boolean.class));
        entity.setDryingOvenMeterNote(converter.fromRow(row, prefix + "_drying_oven_meter_note", String.class));
        entity.setDryingOvenWall(converter.fromRow(row, prefix + "_drying_oven_wall", Boolean.class));
        entity.setDryingOvenWallNote(converter.fromRow(row, prefix + "_drying_oven_wall_note", String.class));
        entity.setDryingOvenValve(converter.fromRow(row, prefix + "_drying_oven_valve", Boolean.class));
        entity.setDryingOvenValveNote(converter.fromRow(row, prefix + "_drying_oven_valve_note", String.class));
        entity.setSieveScreen(converter.fromRow(row, prefix + "_sieve_screen", Boolean.class));
        entity.setSieveScreenNote(converter.fromRow(row, prefix + "_sieve_screen_note", String.class));
        entity.setCrusher(converter.fromRow(row, prefix + "_crusher", Boolean.class));
        entity.setCrusherNote(converter.fromRow(row, prefix + "_crusher_note", String.class));
        entity.setMagnet(converter.fromRow(row, prefix + "_magnet", Boolean.class));
        entity.setMagnetNote(converter.fromRow(row, prefix + "_magnet_note", String.class));
        entity.setMixer(converter.fromRow(row, prefix + "_mixer", Boolean.class));
        entity.setMixerNote(converter.fromRow(row, prefix + "_mixer_note", String.class));
        entity.setPackagingMachine(converter.fromRow(row, prefix + "_packaging_machine", Boolean.class));
        entity.setPackagingMachineNote(converter.fromRow(row, prefix + "_packaging_machine_note", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setWorkItemId(converter.fromRow(row, prefix + "_work_item_id", UUID.class));
        entity.setCheckDate(converter.fromRow(row, prefix + "_check_date", LocalDate.class));
        return entity;
    }
}
