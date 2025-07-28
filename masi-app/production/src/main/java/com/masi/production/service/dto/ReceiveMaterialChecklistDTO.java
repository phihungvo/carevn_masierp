package com.masi.production.service.dto;

import com.masi.production.domain.enumeration.MaterialType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.ReceiveMaterialChecklist;
import com.masi.production.domain.enumeration.ChecklistType;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

/**
 * A DTO for the {@link com.masi.production.domain.ReceiveMaterialChecklist} entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceiveMaterialChecklistDTO extends BaseCheckListDto implements Serializable {


    @NotNull(message = "must not be null")
    private LocalDate checkDate=LocalDate.now();


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedCheckDate;

    @NotNull(message = "must not be null")
    private ZonedDateTime checkTime=ZonedDateTime.now();

    @NotNull(message = "must not be null")
    private String weightNumber="";

    @NotNull(message = "must not be null")
    private Boolean transportCondition=false;

    private String transportNote;

    @NotNull(message = "must not be null")
    private Boolean checkStatus = false;

    private String statusNote;

    @NotNull(message = "must not be null")
    private Boolean checkSmell = false;

    private String smellNote;

    @NotNull(message = "must not be null")
    private Boolean checkImpurity = false;

    private String impurityNote;

    @NotNull(message = "must not be null")
    private Boolean checkPoison = false;

    private String poisonNote;

    @NotNull(message = "must not be null")
    private UUID receiverId = UUID.randomUUID();

    private String note;

    private MaterialType materialType;

    private String weight;



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


    public ReceiveMaterialChecklist toEntity() {
        ReceiveMaterialChecklist receiveMaterialChecklist = new ReceiveMaterialChecklist();
        receiveMaterialChecklist.setId(this.id);
        receiveMaterialChecklist.setCheckDate(this.checkDate);
        receiveMaterialChecklist.setCheckTime(this.checkTime);
        receiveMaterialChecklist.setWeightNumber(this.weightNumber);
        receiveMaterialChecklist.setTransportCondition(this.transportCondition);
        receiveMaterialChecklist.setTransportNote(this.transportNote);
        receiveMaterialChecklist.setCheckStatus(this.checkStatus);
        receiveMaterialChecklist.setStatusNote(this.statusNote);
        receiveMaterialChecklist.setCheckSmell(this.checkSmell);
        receiveMaterialChecklist.setSmellNote(this.smellNote);
        receiveMaterialChecklist.setCheckImpurity(this.checkImpurity);
        receiveMaterialChecklist.setImpurityNote(this.impurityNote);
        receiveMaterialChecklist.setCheckPoison(this.checkPoison);
        receiveMaterialChecklist.setPoisonNote(this.poisonNote);
        receiveMaterialChecklist.setReceiverId(this.receiverId);
        receiveMaterialChecklist.setNote(this.note);
        receiveMaterialChecklist.setIsActive(this.isActive);
        receiveMaterialChecklist.setCreatedAt(this.createdAt);
        receiveMaterialChecklist.setLastUpdated(this.lastUpdated);
        receiveMaterialChecklist.setWorkItemId(this.workItemId);
        receiveMaterialChecklist.setMaterialType(this.materialType);
        receiveMaterialChecklist.setWeight(this.weight);
        return receiveMaterialChecklist;
    }

    public void applyUpdate(ReceiveMaterialChecklist entity) {
        if (entity == null) {
            return;
        }
        entity.setCheckDate(this.checkDate);
        entity.setCheckTime(this.checkTime);
        entity.setWeightNumber(this.weightNumber);
        entity.setTransportCondition(this.transportCondition);
        entity.setTransportNote(this.transportNote);
        entity.setCheckStatus(this.checkStatus);
        entity.setStatusNote(this.statusNote);
        entity.setCheckSmell(this.checkSmell);
        entity.setSmellNote(this.smellNote);
        entity.setCheckImpurity(this.checkImpurity);
        entity.setImpurityNote(this.impurityNote);
        entity.setCheckPoison(this.checkPoison);
        entity.setPoisonNote(this.poisonNote);
        entity.setReceiverId(this.receiverId);
        entity.setNote(this.note);
        entity.setWorkItemId(this.workItemId);
        entity.setMaterialType(this.materialType);
        entity.setWeight(this.weight);
        entity.setIsActive(true);
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setLastUpdated(ZonedDateTime.now());
        entity.setIsPersisted();

    }

    public ReceiveMaterialChecklistDTO() {
        this.type = ChecklistType.RECEIVE_MATERIAL_CHECKLIST.getName();
    }

}
