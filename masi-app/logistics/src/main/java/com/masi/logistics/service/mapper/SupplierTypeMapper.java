package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.SupplierType;
import com.masi.logistics.service.dto.SupplierTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SupplierType} and its DTO {@link SupplierTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierTypeMapper extends EntityMapper<SupplierTypeDTO, SupplierType> {}
