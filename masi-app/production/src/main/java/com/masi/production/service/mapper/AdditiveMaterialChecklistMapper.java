package com.masi.production.service.mapper;

import com.masi.production.domain.AdditiveMaterialChecklist;
import com.masi.production.domain.WorkItem;
import com.masi.production.service.dto.AdditiveMaterialChecklistDTO;
import com.masi.production.service.dto.WorkItemDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdditiveMaterialChecklist} and its DTO {@link AdditiveMaterialChecklistDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdditiveMaterialChecklistMapper extends EntityMapper<AdditiveMaterialChecklistDTO, AdditiveMaterialChecklist> {
    @Mapping(target = "workItem", source = "workItem", qualifiedByName = "workItemId")
    AdditiveMaterialChecklistDTO toDto(AdditiveMaterialChecklist s);

    @Named("workItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkItemDTO toDtoWorkItemId(WorkItem workItem);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
