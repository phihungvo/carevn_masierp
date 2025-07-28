package com.masi.production.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.ReceiveMaterialChecklist;
import com.masi.production.domain.SteamingProcessChecklist;
import com.masi.production.domain.WorkOrder;
import com.masi.production.domain.enumeration.ChecklistType;
import com.masi.production.domain.enumeration.ManufactureOrderType;
import com.masi.production.domain.enumeration.WoStatus;
import com.masi.production.domain.enumeration.WorkOrderType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.WorkOrder} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkOrderDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private LocalDate fromDate;

    private ZonedDateTime zonedFromDate;

    private LocalDate toDate;

    private ZonedDateTime zonedToDate;

    private WoStatus status;

    private Boolean isActive;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WorkItemDTO workItem;
    @JsonIgnore
    private boolean withNewWorkItemSameType = false;

    private UUID workItemId;

    private ManufactureOrderDTO manufactureOrder;

    private WorkOrderType checklistType;

    private ManufactureOrderType manufactureOrderType;

    public Integer getDailyIndex() {
        if (checklistType == null) {
            return 0;
        }
        return checklistType.getOrder();
    }

    public Integer getOrderIndex() {
        if (checklistType == null) {
            return 0;
        }
        return checklistType.getStandardOrder();
    }

    @NotNull(message = "must not be null")
    private UUID moId;

    public BaseCheckListDto getChecklist() {
        BaseCheckListDto checklist = switch (checklistType) {
            case RECEIVE_MATERIAL_CHECKLIST -> new ReceiveMaterialChecklistDTO();
            case STEAMING_PROCESS_CHECKLIST -> new SteamingProcessChecklistDTO();
            case ADDITIVE_MATERIAL_CHECKLIST -> new AdditiveMaterialChecklistDTO();
            case MACHINE_OPERATION_CHECKLIST -> new MachineOperationChecklistDTO();
            case METAL_DETECTION_CHECKLIST -> new MetalDetectionChecklistDTO();
            case MIXING_REPORT_CHECKLIST -> new MixingReportChecklistDTO();
            default -> throw new IllegalArgumentException("Unexpected value: " + checklistType);
        };
        checklist.setWorkItemId(workItemId);
        checklist.setId(id);
        checklist.setCreatedAt(ZonedDateTime.now());
        checklist.setLastUpdated(null);
        checklist.setIsActive(true);
        return checklist;
    }

    @JsonIgnore
    public WorkOrder toEntity() {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setId(this.getId());
        workOrder.setFromDate(this.getFromDate());
        workOrder.setToDate(this.getToDate());
        workOrder.setStatus(this.getStatus());
        workOrder.setIsActive(this.getIsActive());
        workOrder.setCreatedAt(this.getCreatedAt());
        workOrder.setLastUpdated(this.getLastUpdated());
        workOrder.workItem((this.getWorkItem().toEntity()));
        workOrder.setWorkItemId(this.getWorkItem().getId());
        workOrder.setManufactureOrderId(this.getMoId());
        workOrder.setChecklistType(this.getChecklistType());
        workOrder.setManufactureOrderType(this.getManufactureOrderType());
        return workOrder;
    }
}
