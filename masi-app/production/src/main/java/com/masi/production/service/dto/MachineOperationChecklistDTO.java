package com.masi.production.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.MachineOperationChecklist;
import com.masi.production.domain.enumeration.ChecklistType;
import lombok.*;

/**
 * A DTO for the {@link com.masi.production.domain.MachineOperationChecklist} entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MachineOperationChecklistDTO extends BaseCheckListDto implements Serializable {



    @NotNull(message = "must not be null")
    private ZonedDateTime checkTime=ZonedDateTime.now();

    private LocalDate checkDate;

    @NotNull(message = "must not be null")
    private Boolean incineratorAirDuct = false;

    private String incineratorAirDuctNote;

    @NotNull(message = "must not be null")
    private Boolean incinerator=false;

    private String incineratorCheckNote;

    @NotNull(message = "must not be null")
    private Boolean dryingOvenAirDuct = false;

    private String dryingOvenAirDuctNote;

    @NotNull(message = "must not be null")
    private Boolean dryingOvenMeter = false;

    private String dryingOvenMeterNote;

    @NotNull(message = "must not be null")
    private Boolean dryingOvenWall = false;

    private String dryingOvenWallNote;

    @NotNull(message = "must not be null")
    private Boolean dryingOvenValve = false;

    private String dryingOvenValveNote;

    @NotNull(message = "must not be null")
    private Boolean sieveScreen = false;

    private String sieveScreenNote;

    @NotNull(message = "must not be null")
    private Boolean crusher = false;

    private String crusherNote;

    @NotNull(message = "must not be null")
    private Boolean magnet = false;

    private String magnetNote;

    @NotNull(message = "must not be null")
    private Boolean mixer = false;

    private String mixerNote;

    @NotNull(message = "must not be null")
    private Boolean packagingMachine = false;

    private String packagingMachineNote;

    public MachineOperationChecklistDTO() {
        this.type = ChecklistType.MACHINE_OPERATION_CHECKLIST.getName();
        // Empty constructor needed for Jackson.
    }


    public void applyUpdateTo(MachineOperationChecklist entity) {
        entity.setCheckTime(this.checkTime);
        entity.setIncineratorAirDuct(this.incineratorAirDuct);
        entity.setIncineratorAirDuctNote(this.incineratorAirDuctNote);
        entity.setIncinerator(this.incinerator);
        entity.setIncineratorCheckNote(this.incineratorCheckNote);
        entity.setDryingOvenAirDuct(this.dryingOvenAirDuct);
        entity.setDryingOvenAirDuctNote(this.dryingOvenAirDuctNote);
        entity.setDryingOvenMeter(this.dryingOvenMeter);
        entity.setDryingOvenMeterNote(this.dryingOvenMeterNote);
        entity.setDryingOvenWall(this.dryingOvenWall);
        entity.setDryingOvenWallNote(this.dryingOvenWallNote);
        entity.setDryingOvenValve(this.dryingOvenValve);
        entity.setDryingOvenValveNote(this.dryingOvenValveNote);
        entity.setSieveScreen(this.sieveScreen);
        entity.setSieveScreenNote(this.sieveScreenNote);
        entity.setCrusher(this.crusher);
        entity.setCrusherNote(this.crusherNote);
        entity.setMagnet(this.magnet);
        entity.setMagnetNote(this.magnetNote);
        entity.setMixer(this.mixer);
        entity.setMixerNote(this.mixerNote);
        entity.setPackagingMachine(this.packagingMachine);
        entity.setPackagingMachineNote(this.packagingMachineNote);
        entity.setWorkItemId(workItemId);
        entity.setCheckDate(checkDate);
    }

    public MachineOperationChecklist toEntity() {
        MachineOperationChecklist machineOperationChecklist = new MachineOperationChecklist();
        machineOperationChecklist.setId(id);
        machineOperationChecklist.setCheckTime(checkTime);
        machineOperationChecklist.setIncineratorAirDuct(incineratorAirDuct);
        machineOperationChecklist.setIncineratorAirDuctNote(incineratorAirDuctNote);
        machineOperationChecklist.setIncinerator(incinerator);
        machineOperationChecklist.setIncineratorCheckNote(incineratorCheckNote);
        machineOperationChecklist.setDryingOvenAirDuct(dryingOvenAirDuct);
        machineOperationChecklist.setDryingOvenAirDuctNote(dryingOvenAirDuctNote);
        machineOperationChecklist.setDryingOvenMeter(dryingOvenMeter);
        machineOperationChecklist.setDryingOvenMeterNote(dryingOvenMeterNote);
        machineOperationChecklist.setDryingOvenWall(dryingOvenWall);
        machineOperationChecklist.setDryingOvenWallNote(dryingOvenWallNote);
        machineOperationChecklist.setDryingOvenValve(dryingOvenValve);
        machineOperationChecklist.setDryingOvenValveNote(dryingOvenValveNote);
        machineOperationChecklist.setSieveScreen(sieveScreen);
        machineOperationChecklist.setSieveScreenNote(sieveScreenNote);
        machineOperationChecklist.setCrusher(crusher);
        machineOperationChecklist.setCrusherNote(crusherNote);
        machineOperationChecklist.setMagnet(magnet);
        machineOperationChecklist.setMagnetNote(magnetNote);
        machineOperationChecklist.setMixer(mixer);
        machineOperationChecklist.setMixerNote(mixerNote);
        machineOperationChecklist.setPackagingMachine(packagingMachine);
        machineOperationChecklist.setPackagingMachineNote(packagingMachineNote);
        machineOperationChecklist.setWorkItemId(this.getWorkItemId());
        machineOperationChecklist.setIsActive(true);
        machineOperationChecklist.setCreatedAt(ZonedDateTime.now());
        machineOperationChecklist.setLastUpdated(ZonedDateTime.now());
        machineOperationChecklist.setCheckDate(checkDate);
        return machineOperationChecklist;
    }

    public void applyUpdate(MachineOperationChecklist entity) {
        if (entity == null) {
           return;
        }
        entity.setCheckTime(checkTime);
        entity.setIncineratorAirDuct(incineratorAirDuct);
        entity.setIncineratorAirDuctNote(incineratorAirDuctNote);
        entity.setIncinerator(incinerator);
        entity.setIncineratorCheckNote(incineratorCheckNote);
        entity.setDryingOvenAirDuct(dryingOvenAirDuct);
        entity.setDryingOvenAirDuctNote(dryingOvenAirDuctNote);
        entity.setDryingOvenMeter(dryingOvenMeter);
        entity.setDryingOvenMeterNote(dryingOvenMeterNote);
        entity.setDryingOvenWall(dryingOvenWall);
        entity.setDryingOvenWallNote(dryingOvenWallNote);
        entity.setDryingOvenValve(dryingOvenValve);
        entity.setDryingOvenValveNote(dryingOvenValveNote);
        entity.setSieveScreen(sieveScreen);
        entity.setSieveScreenNote(sieveScreenNote);
        entity.setCrusher(crusher);
        entity.setCrusherNote(crusherNote);
        entity.setMagnet(magnet);
        entity.setMagnetNote(magnetNote);
        entity.setMixer(mixer);
        entity.setMixerNote(mixerNote);
        entity.setPackagingMachine(packagingMachine);
        entity.setPackagingMachineNote(packagingMachineNote);
        entity.setWorkItemId(workItemId);
        entity.setCheckDate(checkDate);
        entity.setIsPersisted();
    }


}
