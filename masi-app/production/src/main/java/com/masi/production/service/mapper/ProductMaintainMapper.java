package com.masi.production.service.mapper;

import com.masi.production.domain.ProductMaintain;
import com.masi.production.domain.ProductPackage;
import com.masi.production.service.dto.ProductMaintainDTO;
import com.masi.production.service.dto.ProductPackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductMaintain} and its DTO {@link ProductMaintainDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMaintainMapper extends EntityMapper<ProductMaintainDTO, ProductMaintain> {

    @Mapping(target = "productPackageDTO", source = "productPackage", qualifiedByName = "toProductPackageDTO")
    ProductMaintainDTO toDto(ProductMaintain productMaintain);

    @Named("toProductPackageDTO")
    @BeanMapping(ignoreByDefault = false)
    ProductPackageDTO toProductPackageDTO(ProductPackage productPackage);
}
