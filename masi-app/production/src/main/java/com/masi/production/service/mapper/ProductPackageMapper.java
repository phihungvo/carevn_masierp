package com.masi.production.service.mapper;

import com.masi.production.domain.ProductPackage;
import com.masi.production.domain.WorkOrder;
import com.masi.production.service.dto.ProductPackageDTO;
import com.masi.production.service.dto.WorkOrderDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductPackage} and its DTO {@link ProductPackageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductPackageMapper extends EntityMapper<ProductPackageDTO, ProductPackage> {
    @Mapping(target = "workOrder", source = "workOrder", qualifiedByName = "workOrderId")
    ProductPackageDTO toDto(ProductPackage productPackage);

    @Named("workOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkOrderDTO toDtoWorkOrderId(WorkOrder workOrder);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
