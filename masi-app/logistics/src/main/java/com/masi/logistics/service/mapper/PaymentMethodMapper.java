package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.PaymentMethod;
import com.masi.logistics.service.dto.PaymentMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentMethod} and its DTO {@link PaymentMethodDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {}
