package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.SupplierGroup;
import com.masi.logistics.domain.SupplierType;
import com.masi.logistics.domain.Suppliers;
import com.masi.logistics.service.dto.SupplierGroupDTO;
import com.masi.logistics.service.dto.SupplierTypeDTO;
import com.masi.logistics.service.dto.SuppliersDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Suppliers} and its DTO {@link SuppliersDTO}.
 */
@Mapper(componentModel = "spring")
public interface SuppliersMapper extends EntityMapper<SuppliersDTO, Suppliers> {
    @Mapping(target = "supplierGroup", source = "supplierGroup", qualifiedByName = "supplierGroupId")
    @Mapping(target = "supplierType", source = "supplierType", qualifiedByName = "supplierTypeId")
    SuppliersDTO toDto(Suppliers s);

    @Named("supplierGroupId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    SupplierGroupDTO toDtoSupplierGroupId(SupplierGroup supplierGroup);

    @Named("supplierTypeId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    SupplierTypeDTO toDtoSupplierTypeId(SupplierType supplierType);
}
