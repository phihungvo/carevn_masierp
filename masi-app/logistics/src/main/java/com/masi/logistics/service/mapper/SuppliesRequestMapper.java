package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Suppliers;
import com.masi.logistics.domain.SuppliesRequest;
import com.masi.logistics.domain.SuppliesRequestType;
import com.masi.logistics.service.dto.SuppliersDTO;
import com.masi.logistics.service.dto.SuppliesRequestDTO;
import com.masi.logistics.service.dto.SuppliesRequestTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SuppliesRequest} and its DTO {@link SuppliesRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface SuppliesRequestMapper extends EntityMapper<SuppliesRequestDTO, SuppliesRequest> {
    @Mapping(target = "requestType", source = "requestType", qualifiedByName = "requestType")
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "supplier")
    SuppliesRequestDTO toDto(SuppliesRequest suppliesRequest);

    @Named("requestType")
    @BeanMapping(ignoreByDefault = false)
    SuppliesRequestTypeDTO toSuppliesRequestTypeDto(SuppliesRequestType suppliesRequestType);

    @Named("supplier")
    @BeanMapping(ignoreByDefault = false)
    SuppliersDTO toSuppliersDTO(Suppliers supplier);

}
