package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.domain.enumeration.ChecklistType;
import com.masi.production.domain.enumeration.ManufactureOrderType;
import com.masi.production.domain.enumeration.WoStatus;
import com.masi.production.domain.enumeration.WorkOrderType;
import com.masi.production.service.dto.WorkOrderDTO;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A WorkOrder.
 */
@Data
@Table("work_order")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkOrder implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("from_date")
    private LocalDate fromDate;

    @NotNull(message = "must not be null")
    @Column("to_date")
    private LocalDate toDate;

    @Column("status")
    private WoStatus status;

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
    private WorkItem workItem;

    @Transient
    @JsonIgnoreProperties(value = { "workOrders" }, allowSetters = true)
    private ManufactureOrder manufactureOrder;

    @Column("work_item_id")
    private UUID workItemId;

    @Column("manufacture_order_id")
    private UUID manufactureOrderId;

    @Transient
    @JsonIgnoreProperties(value = { "workOrder" }, allowSetters = true)
    private Collection<ProductPackage> productPackages = new HashSet<>();

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @Column("checklist_type")
    private WorkOrderType checklistType;

    @Column("manufacture_order_type")
    private ManufactureOrderType manufactureOrderType;


    @Transient
    public Integer getDailyIndex() {
        if (checklistType == null) {
            return 0;
        }
        return checklistType.getOrder();
    }
    @Transient

    public Integer getOrderIndex() {
        if (checklistType == null) {
            return 0;
        }
        return checklistType.getStandardOrder();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public WorkOrder id(UUID id) {
        this.setId(id);
        return this;
    }

    public WorkOrder fromDate(LocalDate fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public WorkOrder toDate(LocalDate toDate) {
        this.setToDate(toDate);
        return this;
    }

    public WorkOrder status(WoStatus status) {
        this.setStatus(status);
        return this;
    }

    public WorkOrder isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public WorkOrder createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public WorkOrder lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public WorkOrder setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public WorkOrder workItem(WorkItem workItem) {
        this.setWorkItem(workItem);
        return this;
    }

    public WorkOrder manufactureOrder(ManufactureOrder manufactureOrder) {
        this.setManufactureOrder(manufactureOrder);
        return this;
    }

    public WorkOrder productPackages(Collection<ProductPackage> productPackages) {
        this.setProductPackages(productPackages);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    public WorkOrderDTO toDto() {
        WorkOrderDTO workOrderDTO = new WorkOrderDTO();
        workOrderDTO.setId(this.getId());
        workOrderDTO.setFromDate(this.getFromDate());
        if (Objects.nonNull(this.fromDate)) {
            workOrderDTO.setZonedFromDate(ZonedDateTime.of(this.fromDate.atStartOfDay(), ZoneOffset.UTC));
        }
        workOrderDTO.setToDate(this.getToDate());
        if (Objects.nonNull(this.toDate)) {
            workOrderDTO.setZonedToDate(ZonedDateTime.of(this.toDate.atStartOfDay(), ZoneOffset.UTC));
        }
        workOrderDTO.setStatus(this.status);
        workOrderDTO.setIsActive(this.isActive);
        workOrderDTO.setCreatedAt(this.createdAt);
        workOrderDTO.setLastUpdated(this.lastUpdated);
        workOrderDTO.setWorkItemId(this.workItemId);
        workOrderDTO.setManufactureOrderType(this.manufactureOrderType);
        try {
            if (this.checklistType != null) {
                workOrderDTO.setChecklistType(this.checklistType);
            } else {
                workOrderDTO.setChecklistType(WorkOrderType.ADDITIVE_MATERIAL_CHECKLIST); // Assuming DEFAULT is a valid enum value
            }        } catch (Exception e) {

        }
        if (Objects.nonNull(workItem))
            workOrderDTO.setWorkItem(this.getWorkItem().toDto());

        workOrderDTO.setMoId(this.getManufactureOrderId());
        return workOrderDTO;
    }

    public void partialUpdate(WorkOrderDTO workOrderDTO) {
        this.setFromDate(workOrderDTO.getFromDate());
        this.setManufactureOrderId(workOrderDTO.getMoId());
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public boolean isPersisted() {
        return isPersisted;
    }

    public void setPersisted(boolean isPersisted) {
        this.isPersisted = isPersisted;
    }

}
