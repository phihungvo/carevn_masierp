package com.masi.production.service.dto;

import com.masi.production.domain.enumeration.WeightUnit;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.MixingReportChecklist;
import com.masi.production.domain.enumeration.ChecklistType;
import lombok.*;

/**
 * A DTO for the {@link com.masi.production.domain.MixingReportChecklist} entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MixingReportChecklistDTO extends BaseCheckListDto implements Serializable {



    private LocalDate checkDate=LocalDate.now();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedCheckDate;


    private String finProduct1No = "";


    private Float finProduct1Weight ;

    private WeightUnit finProduct1WeightUnit;

    private String finProduct2No = "";

    private Float finProduct2Weight ;

    private WeightUnit finProduct2WeightUnit;

    private String bhtNo = "";

    private Float bhtWeight ;

    private WeightUnit bhtWeightUnit;

    private Float bhtWeightPrd;

    private WeightUnit bhtWeightPrdUnit;

    private String weightPrdNo = "";

    private Boolean checkImpurity;

    private String checkImpurityNote;

    private Boolean checkSmell;

    private String checkSmellNote;

    private Boolean checkColor;

    private String checkColorNote;

    private String checkEmployeeId;

    private String note;

    private Float moisture ;

    private Float tvn ;

    private Float ash ;

    private Float protein;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void applyUpdateTo(MixingReportChecklist entity) {
        entity.setCheckDate(this.checkDate);
        entity.setFinProduct1No(this.finProduct1No);
        entity.setFinProduct1Weight(this.finProduct1Weight);
        entity.setFinProduct2No(this.finProduct2No);
        entity.setFinProduct2Weight(this.finProduct2Weight);
        entity.setBhtNo(this.bhtNo);
        entity.setBhtWeight(this.bhtWeight);
        entity.setBhtWeightPrd(this.bhtWeightPrd);
        entity.setWeightPrdNo(this.weightPrdNo);
        entity.setCheckImpurity(this.checkImpurity);
        entity.setCheckImpurityNote(this.checkImpurityNote);
        entity.setCheckSmell(this.checkSmell);
        entity.setCheckSmellNote(this.checkSmellNote);
        entity.setCheckColor(this.checkColor);
        entity.setCheckColorNote(this.checkColorNote);
        entity.setCheckEmployeeId(this.checkEmployeeId);
        entity.setMoisture(this.moisture);
        entity.setTvn(this.tvn);
        entity.setAsh(this.ash);
        entity.setProtein(this.protein);
        entity.setNote(this.note);
        // set unit
        entity.setFinProduct1WeightUnit(this.finProduct1WeightUnit);
        entity.setFinProduct2WeightUnit(this.finProduct2WeightUnit);
        entity.setBhtWeightUnit(this.bhtWeightUnit);
        entity.setBhtWeightPrdUnit(this.bhtWeightPrdUnit);


    }

    public WorkItemDTO getWorkItem() {
        return workItem;
    }

    public void setWorkItem(WorkItemDTO workItem) {
        this.workItem = workItem;
    }

    public MixingReportChecklist toEntity() {
        MixingReportChecklist entity = new MixingReportChecklist();
        entity.setId(this.id);
        entity.setCheckDate(this.checkDate);
        entity.setFinProduct1No(this.finProduct1No);
        entity.setFinProduct1Weight(this.finProduct1Weight);
        entity.setFinProduct2No(this.finProduct2No);
        entity.setFinProduct2Weight(this.finProduct2Weight);
        entity.setBhtNo(this.bhtNo);
        entity.setBhtWeight(this.bhtWeight);
        entity.setBhtWeightPrd(this.bhtWeightPrd);
        entity.setWeightPrdNo(this.weightPrdNo);
        entity.setCheckImpurity(this.checkImpurity);
        entity.setCheckImpurityNote(this.checkImpurityNote);
        entity.setCheckSmell(this.checkSmell);
        entity.setCheckSmellNote(this.checkSmellNote);
        entity.setCheckColor(this.checkColor);
        entity.setCheckColorNote(this.checkColorNote);
        entity.setCheckEmployeeId(this.checkEmployeeId);
        entity.setMoisture(this.moisture);
        entity.setTvn(this.tvn);
        entity.setAsh(this.ash);
        entity.setProtein(this.protein);
        entity.setNote(this.note);
        entity.setWorkItemId(this.getWorkItemId());
        // set unit
        entity.setFinProduct1WeightUnit(this.finProduct1WeightUnit);
        entity.setFinProduct2WeightUnit(this.finProduct2WeightUnit);
        entity.setBhtWeightUnit(this.bhtWeightUnit);
        entity.setBhtWeightPrdUnit(this.bhtWeightPrdUnit);
        entity.setIsActive(true);
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setLastUpdated(ZonedDateTime.now());

        return entity;
    }

    public MixingReportChecklistDTO() {
        this.type = ChecklistType.MIXING_REPORT_CHECKLIST.getName();
    }
}
