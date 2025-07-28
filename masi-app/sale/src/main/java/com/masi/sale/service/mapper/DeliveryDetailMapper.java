package com.masi.sale.service.mapper;

import com.masi.sale.domain.DeliveryDetail;
import com.masi.sale.domain.Material;
import com.masi.sale.domain.Order;
import com.masi.sale.service.dto.DeliveryDetailDTO;
import com.masi.sale.service.dto.MaterialDTO;
import com.masi.sale.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryDetail} and its DTO {@link DeliveryDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeliveryDetailMapper extends EntityMapper<DeliveryDetailDTO, DeliveryDetail> {

        @Mapping(target = "material", source = "material", qualifiedByName = "materialId")
        @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
        DeliveryDetailDTO toDto(DeliveryDetail s);

        @Named("materialId")
        @BeanMapping(ignoreByDefault = false)
        MaterialDTO toMaterialIdDtoId(Material material);

        @Named("orderId")
        @BeanMapping(ignoreByDefault = false)
        OrderDTO toOrderDtoId(Order order);
}
