package com.masi.sale.service.mapper;

import com.masi.sale.domain.PurchaseRequest;
import com.masi.sale.domain.PurchaseReview;
import com.masi.sale.service.dto.PurchaseRequestDTO;
import com.masi.sale.service.dto.PurchaseReviewDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseReview} and its DTO {@link PurchaseReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchaseReviewMapper extends EntityMapper<PurchaseReviewDTO, PurchaseReview> {
    @Mapping(target = "purchaseRequest", source = "purchaseRequest", qualifiedByName = "purchaseRequestId")
    PurchaseReviewDTO toDto(PurchaseReview s);

    @Named("purchaseRequestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PurchaseRequestDTO toDtoPurchaseRequestId(PurchaseRequest purchaseRequest);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
