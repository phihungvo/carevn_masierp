package com.masi.production.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.SteamingProcessChecklist;
import com.masi.production.domain.enumeration.ChecklistType;
import lombok.*;

/**
 * A DTO for the {@link com.masi.production.domain.SteamingProcessChecklist} entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SteamingProcessChecklistDTO extends BaseCheckListDto implements Serializable {

    @NotNull(message = "must not be null")
    private LocalDate checkDate=LocalDate.now();


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedCheckDate;

    @NotNull(message = "must not be null")
    private ZonedDateTime checkTime=ZonedDateTime.now();

    @NotNull(message = "must not be null")
    private String weightNumber="";

    @NotNull(message = "must not be null")
    private String steamerAtm="";

    private String steamerTemp="";

    @NotNull(message = "must not be null")
    private ZonedDateTime steamerTime= ZonedDateTime.now();

    @NotNull(message = "must not be null")
    private String tub1Atm="";

    private String tub1Temp="";

    @NotNull(message = "must not be null")
    private ZonedDateTime tub1Time=ZonedDateTime.now();

    @NotNull(message = "must not be null")
    private String tub2Atm="";

    private String tub2Temp = "";

    @NotNull(message = "must not be null")
    private ZonedDateTime tub2Time = ZonedDateTime.now();

    @NotNull(message = "must not be null")
    private String finProductNo = "";

    @NotNull(message = "must not be null")
    private UUID receiverId= UUID.randomUUID();

    private String note;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getWorkItemId() {
        return workItemId;
    }

    public void setWorkItemId(UUID workItemId) {
        this.workItemId = workItemId;
    }

    public SteamingProcessChecklist toEntity() {
        SteamingProcessChecklist steamingProcessChecklist = new SteamingProcessChecklist();
        steamingProcessChecklist.setId(id);
        steamingProcessChecklist.setCheckDate(checkDate);
        steamingProcessChecklist.setCheckTime(checkTime);
        steamingProcessChecklist.setWeightNumber(weightNumber);
        steamingProcessChecklist.setSteamerAtm(steamerAtm);
        steamingProcessChecklist.setSteamerTemp(steamerTemp);
        steamingProcessChecklist.setSteamerTime(steamerTime);
        steamingProcessChecklist.setTub1Atm(tub1Atm);
        steamingProcessChecklist.setTub1Temp(tub1Temp);
        steamingProcessChecklist.setTub1Time(tub1Time);
        steamingProcessChecklist.setTub2Atm(tub2Atm);
        steamingProcessChecklist.setTub2Temp(tub2Temp);
        steamingProcessChecklist.setTub2Time(tub2Time);
        steamingProcessChecklist.setFinProductNo(finProductNo);
        steamingProcessChecklist.setReceiverId(receiverId);
        steamingProcessChecklist.setNote(note);
        steamingProcessChecklist.setIsActive(isActive);
        steamingProcessChecklist.setCreatedAt(createdAt);
        steamingProcessChecklist.setLastUpdated(lastUpdated);
        steamingProcessChecklist.setWorkItemId(this.workItemId);
        return steamingProcessChecklist;
    }

    public void applyUpdate(SteamingProcessChecklist entity) {
        if (entity == null) {
            return;
        }
        entity.setCheckDate(checkDate);
        entity.setCheckTime(checkTime);
        entity.setWeightNumber(weightNumber);
        entity.setSteamerAtm(steamerAtm);
        entity.setSteamerTemp(steamerTemp);
        entity.setSteamerTime(steamerTime);
        entity.setTub1Atm(tub1Atm);
        entity.setTub1Temp(tub1Temp);
        entity.setTub1Time(tub1Time);
        entity.setTub2Atm(tub2Atm);
        entity.setTub2Temp(tub2Temp);
        entity.setTub2Time(tub2Time);
        entity.setFinProductNo(finProductNo);
        entity.setReceiverId(receiverId);
        entity.setNote(note);
        entity.setWorkItemId(this.workItemId);
        entity.setIsActive(true);
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setLastUpdated(ZonedDateTime.now());
        entity.setIsPersisted();
    }

    public SteamingProcessChecklistDTO() {
        this.type = ChecklistType.STEAMING_PROCESS_CHECKLIST.getName();
    }
}
