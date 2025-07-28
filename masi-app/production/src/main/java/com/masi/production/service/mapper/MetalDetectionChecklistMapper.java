package com.masi.production.service.mapper;

import com.masi.production.domain.MetalDetectionChecklist;
import com.masi.production.domain.WorkItem;
import com.masi.production.service.dto.MetalDetectionChecklistDTO;
import com.masi.production.service.dto.WorkItemDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetalDetectionChecklist} and its DTO {@link MetalDetectionChecklistDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetalDetectionChecklistMapper extends EntityMapper<MetalDetectionChecklistDTO, MetalDetectionChecklist> {
    @Mapping(target = "workItem", source = "workItem", qualifiedByName = "workItemId")
    MetalDetectionChecklistDTO toDto(MetalDetectionChecklist s);

    @Named("workItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkItemDTO toDtoWorkItemId(WorkItem workItem);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
