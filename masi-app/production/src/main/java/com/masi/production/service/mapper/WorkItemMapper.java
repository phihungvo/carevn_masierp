package com.masi.production.service.mapper;

import com.masi.production.domain.WorkItem;
import com.masi.production.service.dto.WorkItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkItem} and its DTO {@link WorkItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkItemMapper extends EntityMapper<WorkItemDTO, WorkItem> {}
