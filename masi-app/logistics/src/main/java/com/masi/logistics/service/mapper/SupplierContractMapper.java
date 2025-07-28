package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.SupplierContract;
import com.masi.logistics.domain.Suppliers;
import com.masi.logistics.domain.SuppliesRequest;
import com.masi.logistics.service.dto.SupplierContractDTO;
import com.masi.logistics.service.dto.SuppliersDTO;
import com.masi.logistics.service.dto.SuppliesRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SupplierContract} and its DTO {@link SupplierContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierContractMapper extends EntityMapper<SupplierContractDTO, SupplierContract> {

    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "supplierId")
    @Mapping(target = "suppliesRequest", source = "suppliesRequest", qualifiedByName = "requestId")
    SupplierContractDTO toDto(SupplierContract supplierContract);

    @Named("supplierId")
    @BeanMapping(ignoreByDefault = false)
    SuppliersDTO supplierToSupplierId(Suppliers suppliers);

    @Named("requestId")
    @BeanMapping(ignoreByDefault = false)
    SuppliesRequestDTO suppliesRequestToRequestId(SuppliesRequest suppliesRequest);


}
