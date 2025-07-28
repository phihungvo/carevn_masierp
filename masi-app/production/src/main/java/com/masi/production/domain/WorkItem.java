package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.service.dto.WorkItemDTO;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A WorkItem.
 */
@Data
@Table("work_item")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkItem implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "workItem", "manufactureOrder" }, allowSetters = true)
    private Collection<ReceiveMaterialChecklist> receiveMaterialChecklist = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "workItem", "manufactureOrder" }, allowSetters = true)
    private Collection<AdditiveMaterialChecklist> additiveMaterialChecklist = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "workItem", "manufactureOrder" }, allowSetters = true)
    private Collection<MachineOperationChecklist> machineOperationChecklist = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "workItem", "manufactureOrder" }, allowSetters = true)
    private Collection<SteamingProcessChecklist> steamingProcessChecklist = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "workItem", "manufactureOrder" }, allowSetters = true)
    private Collection<MetalDetectionChecklist> metalDetectionChecklist = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "workItem", "manufactureOrder" }, allowSetters = true)
    private Collection<MixingReportChecklist> mixingReportChecklist = new HashSet<>();

    @Transient
    private WorkOrder workOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public WorkItem id(UUID id) {
        this.setId(id);
        return this;
    }

    public WorkItem isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public WorkItem createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public WorkItem lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public WorkItem setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public WorkItem receiveMaterialChecklist(Set<ReceiveMaterialChecklist> receiveMaterialChecklist) {
        this.setReceiveMaterialChecklist(receiveMaterialChecklist);
        return this;
    }

    public WorkItem addReceiveMaterialChecklist(ReceiveMaterialChecklist receiveMaterialChecklist) {
        this.receiveMaterialChecklist.add(receiveMaterialChecklist);
        receiveMaterialChecklist.setWorkItem(this);
        return this;
    }

    public WorkItem removeReceiveMaterialChecklist(ReceiveMaterialChecklist receiveMaterialChecklist) {
        this.receiveMaterialChecklist.remove(receiveMaterialChecklist);
        receiveMaterialChecklist.setWorkItem(null);
        return this;
    }

    public WorkItem additiveMaterialChecklist(Set<AdditiveMaterialChecklist> additiveMaterialChecklist) {
        this.setAdditiveMaterialChecklist(additiveMaterialChecklist);
        return this;
    }

    public WorkItem addAdditiveMaterialChecklist(AdditiveMaterialChecklist additiveMaterialChecklist) {
        this.additiveMaterialChecklist.add(additiveMaterialChecklist);
        additiveMaterialChecklist.setWorkItem(this);
        return this;
    }

    public WorkItem removeAdditiveMaterialChecklist(AdditiveMaterialChecklist additiveMaterialChecklist) {
        this.additiveMaterialChecklist.remove(additiveMaterialChecklist);
        additiveMaterialChecklist.setWorkItem(null);
        return this;
    }

    public WorkItem machineOperationChecklist(Set<MachineOperationChecklist> machineOperationChecklist) {
        this.setMachineOperationChecklist(machineOperationChecklist);
        return this;
    }

    public WorkItem addMachineOperationChecklist(MachineOperationChecklist machineOperationChecklist) {
        this.machineOperationChecklist.add(machineOperationChecklist);
        machineOperationChecklist.setWorkItem(this);
        return this;
    }

    public WorkItem removeMachineOperationChecklist(MachineOperationChecklist machineOperationChecklist) {
        this.machineOperationChecklist.remove(machineOperationChecklist);
        machineOperationChecklist.setWorkItem(null);
        return this;
    }

    public WorkItem steamingProcessChecklist(Set<SteamingProcessChecklist> steamingProcessChecklist) {
        this.setSteamingProcessChecklist(steamingProcessChecklist);
        return this;
    }

    public WorkItem addSteamingProcessChecklist(SteamingProcessChecklist steamingProcessChecklist) {
        this.steamingProcessChecklist.add(steamingProcessChecklist);
        steamingProcessChecklist.setWorkItem(this);
        return this;
    }

    public WorkItem removeSteamingProcessChecklist(SteamingProcessChecklist steamingProcessChecklist) {
        this.steamingProcessChecklist.remove(steamingProcessChecklist);
        steamingProcessChecklist.setWorkItem(null);
        return this;
    }

    public WorkItem metalDetectionChecklist(Set<MetalDetectionChecklist> metalDetectionChecklist) {
        this.setMetalDetectionChecklist(metalDetectionChecklist);
        return this;
    }

    public WorkItem addMetalDetectionChecklist(MetalDetectionChecklist metalDetectionChecklist) {
        this.metalDetectionChecklist.add(metalDetectionChecklist);
        metalDetectionChecklist.setWorkItem(this);
        return this;
    }

    public WorkItem removeMetalDetectionChecklist(MetalDetectionChecklist metalDetectionChecklist) {
        this.metalDetectionChecklist.remove(metalDetectionChecklist);
        metalDetectionChecklist.setWorkItem(null);
        return this;
    }

    public WorkItem mixingReportChecklist(Set<MixingReportChecklist> mixingReportChecklist) {
        this.setMixingReportChecklist(mixingReportChecklist);
        return this;
    }

    public WorkItem addMixingReportChecklist(MixingReportChecklist mixingReportChecklist) {
        this.mixingReportChecklist.add(mixingReportChecklist);
        mixingReportChecklist.setWorkItem(this);
        return this;
    }

    public WorkItem removeMixingReportChecklist(MixingReportChecklist mixingReportChecklist) {
        this.mixingReportChecklist.remove(mixingReportChecklist);
        mixingReportChecklist.setWorkItem(null);
        return this;
    }

    public WorkItem workOrder(WorkOrder workOrder) {
        this.setWorkOrder(workOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public WorkItemDTO toDto() {
        WorkItemDTO workItemDTO = new WorkItemDTO();
        workItemDTO.setId(id);
        workItemDTO.setIsActive(isActive);
        workItemDTO.setCreatedAt(createdAt);
        workItemDTO.setLastUpdated(lastUpdated);
        if (!CollectionUtils.isEmpty(this.receiveMaterialChecklist)) {
            workItemDTO.setReceiveMaterialChecklist(this.getReceiveMaterialChecklist().stream().map(ReceiveMaterialChecklist::toDto).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(this.additiveMaterialChecklist)) {
            workItemDTO.setAdditiveMaterialChecklist(this.getAdditiveMaterialChecklist().stream().map(AdditiveMaterialChecklist::toDto).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(this.machineOperationChecklist)) {
            workItemDTO.setMachineOperationChecklist(this.getMachineOperationChecklist().stream().map(MachineOperationChecklist::toDto).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(this.metalDetectionChecklist)) {
            workItemDTO.setMetalDetectionChecklist(this.getMetalDetectionChecklist().stream().map(MetalDetectionChecklist::toDto).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(this.mixingReportChecklist)) {
            workItemDTO.setMixingReportChecklist(this.getMixingReportChecklist().stream().map(MixingReportChecklist::toDto).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(this.steamingProcessChecklist)) {
            workItemDTO.setSteamingProcessChecklist(this.getSteamingProcessChecklist().stream().map(SteamingProcessChecklist::toDto).collect(Collectors.toList()));
        }
        return workItemDTO;
    }
}
