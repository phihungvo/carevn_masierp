package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.PaymentRequest;
import com.masi.logistics.domain.Suppliers;
import com.masi.logistics.service.dto.PaymentRequestDTO;
import com.masi.logistics.service.dto.SuppliersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentRequest} and its DTO {@link PaymentRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentRequestMapper extends EntityMapper<PaymentRequestDTO, PaymentRequest> {
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierId")
    PaymentRequestDTO toDto(PaymentRequest paymentRequest);

    @Named("supplierId")
    @BeanMapping(ignoreByDefault = false)
    SuppliersDTO toDtoSupplierId(Suppliers suppliers);
}
