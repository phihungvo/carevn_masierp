package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.service.dto.SteamingProcessChecklistDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SteamingProcessChecklist.
 */
@Data
@Table("steaming_process_checklist")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SteamingProcessChecklist implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("check_date")
    private LocalDate checkDate;

    @NotNull(message = "must not be null")
    @Column("check_time")
    private ZonedDateTime checkTime;

    @NotNull(message = "must not be null")
    @Column("weight_number")
    private String weightNumber;

    @NotNull(message = "must not be null")
    @Column("steamer_atm")
    private String steamerAtm;

    @NotNull(message = "must not be null")
    @Column("steamer_temp")
    private String steamerTemp;

    @NotNull(message = "must not be null")
    @Column("steamer_time")
    private ZonedDateTime steamerTime;

    @NotNull(message = "must not be null")
    @Column("tub_1_atm")
    private String tub1Atm;

    @NotNull(message = "must not be null")
    @Column("tub_1_temp")
    private String tub1Temp;

    @NotNull(message = "must not be null")
    @Column("tub_1_time")
    private ZonedDateTime tub1Time;

    @NotNull(message = "must not be null")
    @Column("tub_2_atm")
    private String tub2Atm;

    @NotNull(message = "must not be null")
    @Column("tub_2_temp")
    private String tub2Temp;

    @NotNull(message = "must not be null")
    @Column("tub_2_time")
    private ZonedDateTime tub2Time;

    @NotNull(message = "must not be null")
    @Column("fin_product_no")
    private String finProductNo;

    @NotNull(message = "must not be null")
    @Column("receiver_id")
    private UUID receiverId;

    @Column("note")
    private String note;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SteamingProcessChecklist id(UUID id) {
        this.setId(id);
        return this;
    }

    public SteamingProcessChecklist checkDate(LocalDate checkDate) {
        this.setCheckDate(checkDate);
        return this;
    }

    public SteamingProcessChecklist checkTime(ZonedDateTime checkTime) {
        this.setCheckTime(checkTime);
        return this;
    }

    public SteamingProcessChecklist weightNumber(String weightNumber) {
        this.setWeightNumber(weightNumber);
        return this;
    }

    public SteamingProcessChecklist steamerAtm(String steamerAtm) {
        this.setSteamerAtm(steamerAtm);
        return this;
    }

    public SteamingProcessChecklist steamerTemp(String steamerTemp) {
        this.setSteamerTemp(steamerTemp);
        return this;
    }

    public SteamingProcessChecklist steamerTime(ZonedDateTime steamerTime) {
        this.setSteamerTime(steamerTime);
        return this;
    }

    public SteamingProcessChecklist tub1Atm(String tub1Atm) {
        this.setTub1Atm(tub1Atm);
        return this;
    }

    public SteamingProcessChecklist tub1Temp(String tub1Temp) {
        this.setTub1Temp(tub1Temp);
        return this;
    }

    public SteamingProcessChecklist tub1Time(ZonedDateTime tub1Time) {
        this.setTub1Time(tub1Time);
        return this;
    }

    public SteamingProcessChecklist tub2Atm(String tub2Atm) {
        this.setTub2Atm(tub2Atm);
        return this;
    }

    public SteamingProcessChecklist tub2Temp(String tub2Temp) {
        this.setTub2Temp(tub2Temp);
        return this;
    }

    public SteamingProcessChecklist tub2Time(ZonedDateTime tub2Time) {
        this.setTub2Time(tub2Time);
        return this;
    }

    public SteamingProcessChecklist finProductNo(String finProductNo) {
        this.setFinProductNo(finProductNo);
        return this;
    }

    public SteamingProcessChecklist receiverId(UUID receiverId) {
        this.setReceiverId(receiverId);
        return this;
    }

    public SteamingProcessChecklist note(String note) {
        this.setNote(note);
        return this;
    }

    public SteamingProcessChecklist isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public SteamingProcessChecklist createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public SteamingProcessChecklist lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public SteamingProcessChecklist setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public SteamingProcessChecklist workItem(WorkItem workItem) {
        this.setWorkItem(workItem);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public SteamingProcessChecklistDTO toDto() {
        SteamingProcessChecklistDTO dto = new SteamingProcessChecklistDTO();
        dto.setId(this.id);
        dto.setCheckDate(this.checkDate);
        if (Objects.nonNull(this.checkDate)) {
            dto.setZonedCheckDate(this.checkDate.atStartOfDay().atZone(ZoneOffset.UTC));
        }
        dto.setCheckTime(this.checkTime);
        dto.setWeightNumber(this.weightNumber);
        dto.setSteamerAtm(this.steamerAtm);
        dto.setSteamerTemp(this.steamerTemp);
        dto.setSteamerTime(this.steamerTime);
        dto.setTub1Atm(this.tub1Atm);
        dto.setTub1Temp(this.tub1Temp);
        dto.setTub1Time(this.tub1Time);
        dto.setTub2Atm(this.tub2Atm);
        dto.setTub2Temp(this.tub2Temp);
        dto.setTub2Time(this.tub2Time);
        dto.setFinProductNo(this.finProductNo);
        dto.setReceiverId(this.receiverId);
        dto.setNote(this.note);
        dto.setIsActive(this.isActive);
        dto.setCreatedAt(this.createdAt);
        dto.setLastUpdated(this.lastUpdated);
        dto.setWorkItemId(this.workItemId);
        return dto;
    }
}
