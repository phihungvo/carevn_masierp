package com.masi.production.service.mapper;

import com.masi.production.domain.ManufactureOrder;
import com.masi.production.domain.WorkItem;
import com.masi.production.domain.WorkOrder;
import com.masi.production.service.dto.ManufactureOrderDTO;
import com.masi.production.service.dto.WorkItemDTO;
import com.masi.production.service.dto.WorkOrderDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkOrder} and its DTO {@link WorkOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkOrderMapper extends EntityMapper<WorkOrderDTO, WorkOrder> {
    @Mapping(target = "workItem", source = "workItem", qualifiedByName = "workItemId")
    @Mapping(target = "manufactureOrder", source = "manufactureOrder", qualifiedByName = "manufactureOrderId")
    WorkOrderDTO toDto(WorkOrder s);

    @Named("workItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkItemDTO toDtoWorkItemId(WorkItem workItem);

    @Named("manufactureOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ManufactureOrderDTO toDtoManufactureOrderId(ManufactureOrder manufactureOrder);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
