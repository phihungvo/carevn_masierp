package com.masi.production.service.mapper;

import com.masi.production.domain.MixingReportChecklist;
import com.masi.production.domain.WorkItem;
import com.masi.production.service.dto.MixingReportChecklistDTO;
import com.masi.production.service.dto.WorkItemDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MixingReportChecklist} and its DTO {@link MixingReportChecklistDTO}.
 */
@Mapper(componentModel = "spring")
public interface MixingReportChecklistMapper extends EntityMapper<MixingReportChecklistDTO, MixingReportChecklist> {
    @Mapping(target = "workItem", source = "workItem", qualifiedByName = "workItemId")
    MixingReportChecklistDTO toDto(MixingReportChecklist s);

    @Named("workItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkItemDTO toDtoWorkItemId(WorkItem workItem);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
