package com.masi.sale.service.mapper;

import com.masi.sale.domain.PurchaseRequest;
import com.masi.sale.service.dto.PurchaseRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseRequest} and its DTO {@link PurchaseRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchaseRequestMapper extends EntityMapper<PurchaseRequestDTO, PurchaseRequest> {}
