package com.masi.production.service.mapper;

import com.masi.production.domain.*;
import com.masi.production.service.dto.*;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductRouting} and its DTO {@link ProductRoutingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductRoutingMapper extends EntityMapper<ProductRoutingDTO, ProductRouting> {

    @Mapping(target = "productMaintainDTO", source = "productMaintain", qualifiedByName = "toProductMaintainId")
    ProductRoutingDTO toDto(ProductRouting productRouting);

    @Named("toProductMaintainId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "productPackageDTO", source = "productPackage", qualifiedByName = "productPackage")
    ProductMaintainDTO toProductMaintainId(ProductMaintain productMaintain);

    @Named("productPackage")
    @BeanMapping(ignoreByDefault = false)
    ProductPackageDTO toProductPackage(ProductPackage productPackage);
}
