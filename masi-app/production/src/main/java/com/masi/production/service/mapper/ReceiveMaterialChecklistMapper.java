package com.masi.production.service.mapper;

import com.masi.production.domain.ReceiveMaterialChecklist;
import com.masi.production.domain.WorkItem;
import com.masi.production.service.dto.ReceiveMaterialChecklistDTO;
import com.masi.production.service.dto.WorkItemDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReceiveMaterialChecklist} and its DTO {@link ReceiveMaterialChecklistDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReceiveMaterialChecklistMapper extends EntityMapper<ReceiveMaterialChecklistDTO, ReceiveMaterialChecklist> {
    @Mapping(target = "workItem", source = "workItem", qualifiedByName = "workItemId")
    ReceiveMaterialChecklistDTO toDto(ReceiveMaterialChecklist s);

    @Named("workItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkItemDTO toDtoWorkItemId(WorkItem workItem);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
