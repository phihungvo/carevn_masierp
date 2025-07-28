package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.service.dto.MachineOperationChecklistDTO;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A MachineOperationChecklist.
 */
@Data
@Table("machine_operation_checklist")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MachineOperationChecklist implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("check_date")
    private LocalDate checkDate;


    @NotNull(message = "must not be null")
    @Column("check_time")
    private ZonedDateTime checkTime;

    @NotNull(message = "must not be null")
    @Column("incinerator_air_duct")
    private Boolean incineratorAirDuct;

    @Column("incinerator_air_duct_note")
    private String incineratorAirDuctNote;

    @NotNull(message = "must not be null")
    @Column("incinerator")
    private Boolean incinerator;

    @Column("incinerator_check_note")
    private String incineratorCheckNote;

    @NotNull(message = "must not be null")
    @Column("drying_oven_air_duct")
    private Boolean dryingOvenAirDuct;

    @Column("drying_oven_air_duct_note")
    private String dryingOvenAirDuctNote;

    @NotNull(message = "must not be null")
    @Column("drying_oven_meter")
    private Boolean dryingOvenMeter;

    @Column("drying_oven_meter_note")
    private String dryingOvenMeterNote;

    @NotNull(message = "must not be null")
    @Column("drying_oven_wall")
    private Boolean dryingOvenWall;

    @Column("drying_oven_wall_note")
    private String dryingOvenWallNote;

    @NotNull(message = "must not be null")
    @Column("drying_oven_valve")
    private Boolean dryingOvenValve;

    @Column("drying_oven_valve_note")
    private String dryingOvenValveNote;

    @NotNull(message = "must not be null")
    @Column("sieve_screen")
    private Boolean sieveScreen;

    @Column("sieve_screen_note")
    private String sieveScreenNote;

    @NotNull(message = "must not be null")
    @Column("crusher")
    private Boolean crusher;

    @Column("crusher_note")
    private String crusherNote;

    @NotNull(message = "must not be null")
    @Column("magnet")
    private Boolean magnet;

    @Column("magnet_note")
    private String magnetNote;

    @NotNull(message = "must not be null")
    @Column("mixer")
    private Boolean mixer;

    @Column("mixer_note")
    private String mixerNote;

    @NotNull(message = "must not be null")
    @Column("packaging_machine")
    private Boolean packagingMachine;

    @Column("packaging_machine_note")
    private String packagingMachineNote;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Column("work_item_id")
    private UUID workItemId;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "receiveMaterialChecklists",
            "additiveMaterialChecklists",
            "machineOperationChecklists",
            "steamingProcessChecklists",
            "metalDetectionChecklists",
            "mixingReportChecklists",
            "workOrder",
        },
        allowSetters = true
    )
    private WorkItem workItem;

    // jhipster-needle-entity-add-field - JHip

    public MachineOperationChecklistDTO toDto() {
        MachineOperationChecklistDTO dto = new MachineOperationChecklistDTO();
        dto.setId(this.getId());
        dto.setCheckTime(this.getCheckTime());
        dto.setIncineratorAirDuct(this.getIncineratorAirDuct());
        dto.setIncineratorAirDuctNote(this.getIncineratorAirDuctNote());
        dto.setIncinerator(this.getIncinerator());
        dto.setIncineratorCheckNote(this.getIncineratorCheckNote());
        dto.setDryingOvenAirDuct(this.getDryingOvenAirDuct());
        dto.setDryingOvenAirDuctNote(this.getDryingOvenAirDuctNote());
        dto.setDryingOvenMeter(this.getDryingOvenMeter());
        dto.setDryingOvenMeterNote(this.getDryingOvenMeterNote());
        dto.setDryingOvenWall(this.getDryingOvenWall());
        dto.setDryingOvenWallNote(this.getDryingOvenWallNote());
        dto.setDryingOvenValve(this.getDryingOvenValve());
        dto.setDryingOvenValveNote(this.getDryingOvenValveNote());
        dto.setSieveScreen(this.getSieveScreen());
        dto.setSieveScreenNote(this.getSieveScreenNote());
        dto.setCrusher(this.getCrusher());
        dto.setCrusherNote(this.getCrusherNote());
        dto.setMagnet(this.getMagnet());
        dto.setMagnetNote(this.getMagnetNote());
        dto.setMixer(this.getMixer());
        dto.setMixerNote(this.getMixerNote());
        dto.setPackagingMachine(this.getPackagingMachine());
        dto.setPackagingMachineNote(this.getPackagingMachineNote());
        dto.setIsActive(this.getIsActive());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setLastUpdated(this.getLastUpdated());
        dto.setWorkItemId(this.getWorkItemId());
        dto.setCheckDate(this.getCheckDate());
        return dto;
    }

    public MachineOperationChecklist id(UUID id) {
        this.setId(id);
        return this;
    }

    public MachineOperationChecklist checkTime(ZonedDateTime checkTime) {
        this.setCheckTime(checkTime);
        return this;
    }

    public MachineOperationChecklist incineratorAirDuct(Boolean incineratorAirDuct) {
        this.setIncineratorAirDuct(incineratorAirDuct);
        return this;
    }

    public MachineOperationChecklist incineratorAirDuctNote(String incineratorAirDuctNote) {
        this.setIncineratorAirDuctNote(incineratorAirDuctNote);
        return this;
    }

    public MachineOperationChecklist incinerator(Boolean incinerator) {
        this.setIncinerator(incinerator);
        return this;
    }

    public MachineOperationChecklist incineratorCheckNote(String incineratorCheckNote) {
        this.setIncineratorCheckNote(incineratorCheckNote);
        return this;
    }

    public MachineOperationChecklist dryingOvenAirDuct(Boolean dryingOvenAirDuct) {
        this.setDryingOvenAirDuct(dryingOvenAirDuct);
        return this;
    }

    public MachineOperationChecklist dryingOvenAirDuctNote(String dryingOvenAirDuctNote) {
        this.setDryingOvenAirDuctNote(dryingOvenAirDuctNote);
        return this;
    }

    public MachineOperationChecklist dryingOvenMeter(Boolean dryingOvenMeter) {
        this.setDryingOvenMeter(dryingOvenMeter);
        return this;
    }

    public MachineOperationChecklist dryingOvenMeterNote(String dryingOvenMeterNote) {
        this.setDryingOvenMeterNote(dryingOvenMeterNote);
        return this;
    }

    public MachineOperationChecklist dryingOvenWall(Boolean dryingOvenWall) {
        this.setDryingOvenWall(dryingOvenWall);
        return this;
    }

    public MachineOperationChecklist dryingOvenWallNote(String dryingOvenWallNote) {
        this.setDryingOvenWallNote(dryingOvenWallNote);
        return this;
    }

    public MachineOperationChecklist dryingOvenValve(Boolean dryingOvenValve) {
        this.setDryingOvenValve(dryingOvenValve);
        return this;
    }

    public MachineOperationChecklist dryingOvenValveNote(String dryingOvenValveNote) {
        this.setDryingOvenValveNote(dryingOvenValveNote);
        return this;
    }

    public MachineOperationChecklist sieveScreen(Boolean sieveScreen) {
        this.setSieveScreen(sieveScreen);
        return this;
    }

    public MachineOperationChecklist sieveScreenNote(String sieveScreenNote) {
        this.setSieveScreenNote(sieveScreenNote);
        return this;
    }

    public MachineOperationChecklist crusher(Boolean crusher) {
        this.setCrusher(crusher);
        return this;
    }

    public MachineOperationChecklist crusherNote(String crusherNote) {
        this.setCrusherNote(crusherNote);
        return this;
    }

    public MachineOperationChecklist magnet(Boolean magnet) {
        this.setMagnet(magnet);
        return this;
    }

    public MachineOperationChecklist magnetNote(String magnetNote) {
        this.setMagnetNote(magnetNote);
        return this;
    }

    public MachineOperationChecklist mixer(Boolean mixer) {
        this.setMixer(mixer);
        return this;
    }

    public MachineOperationChecklist mixerNote(String mixerNote) {
        this.setMixerNote(mixerNote);
        return this;
    }

    public MachineOperationChecklist packagingMachine(Boolean packagingMachine) {
        this.setPackagingMachine(packagingMachine);
        return this;
    }

    public MachineOperationChecklist packagingMachineNote(String packagingMachineNote) {
        this.setPackagingMachineNote(packagingMachineNote);
        return this;
    }

    public MachineOperationChecklist isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public MachineOperationChecklist createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public MachineOperationChecklist lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public MachineOperationChecklist setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public MachineOperationChecklist workItem(WorkItem workItem) {
        this.setWorkItem(workItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
