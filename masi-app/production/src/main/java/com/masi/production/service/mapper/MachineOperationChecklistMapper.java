package com.masi.production.service.mapper;

import com.masi.production.domain.MachineOperationChecklist;
import com.masi.production.domain.WorkItem;
import com.masi.production.service.dto.MachineOperationChecklistDTO;
import com.masi.production.service.dto.WorkItemDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MachineOperationChecklist} and its DTO {@link MachineOperationChecklistDTO}.
 */
@Mapper(componentModel = "spring")
public interface MachineOperationChecklistMapper extends EntityMapper<MachineOperationChecklistDTO, MachineOperationChecklist> {
    @Mapping(target = "workItem", source = "workItem", qualifiedByName = "workItemId")
    MachineOperationChecklistDTO toDto(MachineOperationChecklist s);

    @Named("workItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkItemDTO toDtoWorkItemId(WorkItem workItem);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
