package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.SuppliesRequestType;
import com.masi.logistics.service.dto.SuppliesRequestTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SuppliesRequestType} and its DTO {@link SuppliesRequestTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface SuppliesRequestTypeMapper extends EntityMapper<SuppliesRequestTypeDTO, SuppliesRequestType> {}
