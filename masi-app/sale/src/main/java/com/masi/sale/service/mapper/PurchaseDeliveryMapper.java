package com.masi.sale.service.mapper;

import com.masi.sale.domain.PurchaseDelivery;
import com.masi.sale.domain.PurchaseRequest;
import com.masi.sale.service.dto.PurchaseDeliveryDTO;
import com.masi.sale.service.dto.PurchaseRequestDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseDelivery} and its DTO {@link PurchaseDeliveryDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchaseDeliveryMapper extends EntityMapper<PurchaseDeliveryDTO, PurchaseDelivery> {
    @Mapping(target = "purchaseRequest", source = "purchaseRequest", qualifiedByName = "purchaseRequestId")
    PurchaseDeliveryDTO toDto(PurchaseDelivery s);

    @Named("purchaseRequestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PurchaseRequestDTO toDtoPurchaseRequestId(PurchaseRequest purchaseRequest);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
