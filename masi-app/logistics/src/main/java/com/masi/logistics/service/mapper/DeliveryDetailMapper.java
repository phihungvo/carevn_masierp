package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.DeliveryDetail;
import com.masi.logistics.domain.DeliverySchedule;
import com.masi.logistics.domain.Item;
import com.masi.logistics.service.dto.DeliveryDetailDTO;
import com.masi.logistics.service.dto.DeliveryScheduleDTO;
import com.masi.logistics.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryDetail} and its DTO {@link DeliveryDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeliveryDetailMapper extends EntityMapper<DeliveryDetailDTO, DeliveryDetail> {
    @Mapping(target = "deliverySchedule", source = "deliverySchedule", qualifiedByName = "deliveryId")
    @Mapping(target = "contractMaterial", source = "contractMaterial", qualifiedByName = "itemId")
    DeliveryDetailDTO toDto(DeliveryDetail s);

    @Named("deliveryId")
    @BeanMapping(ignoreByDefault = false)
    DeliveryScheduleDTO toDeliveryId(DeliverySchedule deliverySchedule);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = false)
    ItemDTO toItemId(Item item);

}
