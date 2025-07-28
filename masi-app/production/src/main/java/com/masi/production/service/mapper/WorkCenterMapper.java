package com.masi.production.service.mapper;

import com.masi.production.domain.WorkCenter;
import com.masi.production.service.dto.WorkCenterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkCenter} and its DTO {@link WorkCenterDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkCenterMapper extends EntityMapper<WorkCenterDTO, WorkCenter> {}
