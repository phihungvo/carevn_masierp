package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.SupplierGroup;
import com.masi.logistics.service.dto.SupplierGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SupplierGroup} and its DTO {@link SupplierGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierGroupMapper extends EntityMapper<SupplierGroupDTO, SupplierGroup> {}
