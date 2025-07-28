package com.masi.production.service.mapper;

import com.masi.production.domain.SteamingProcessChecklist;
import com.masi.production.domain.WorkItem;
import com.masi.production.service.dto.SteamingProcessChecklistDTO;
import com.masi.production.service.dto.WorkItemDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SteamingProcessChecklist} and its DTO {@link SteamingProcessChecklistDTO}.
 */
@Mapper(componentModel = "spring")
public interface SteamingProcessChecklistMapper extends EntityMapper<SteamingProcessChecklistDTO, SteamingProcessChecklist> {
    @Mapping(target = "workItem", source = "workItem", qualifiedByName = "workItemId")
    SteamingProcessChecklistDTO toDto(SteamingProcessChecklist s);

    @Named("workItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkItemDTO toDtoWorkItemId(WorkItem workItem);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
