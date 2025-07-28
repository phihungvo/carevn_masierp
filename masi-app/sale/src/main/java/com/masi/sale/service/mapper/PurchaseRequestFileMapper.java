package com.masi.sale.service.mapper;

import com.masi.sale.domain.PurchaseRequest;
import com.masi.sale.domain.PurchaseRequestFile;
import com.masi.sale.service.dto.PurchaseRequestDTO;
import com.masi.sale.service.dto.PurchaseRequestFileDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseRequestFile} and its DTO {@link PurchaseRequestFileDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchaseRequestFileMapper extends EntityMapper<PurchaseRequestFileDTO, PurchaseRequestFile> {
    @Mapping(target = "purchaseRequest", source = "purchaseRequest", qualifiedByName = "purchaseRequestId")
    PurchaseRequestFileDTO toDto(PurchaseRequestFile s);

    @Named("purchaseRequestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PurchaseRequestDTO toDtoPurchaseRequestId(PurchaseRequest purchaseRequest);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
