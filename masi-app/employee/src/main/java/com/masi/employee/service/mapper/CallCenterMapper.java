package com.masi.employee.service.mapper;

import com.masi.employee.domain.CallCenter;
import com.masi.employee.service.dto.CallCenterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CallCenter} and its DTO {@link CallCenterDTO}.
 */
@Mapper(componentModel = "spring")
public interface CallCenterMapper extends EntityMapper<CallCenterDTO, CallCenter> {}
