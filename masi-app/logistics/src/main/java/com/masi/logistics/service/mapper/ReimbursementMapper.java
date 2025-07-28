package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.PaymentRequest;
import com.masi.logistics.domain.Reimbursement;
import com.masi.logistics.service.dto.PaymentRequestDTO;
import com.masi.logistics.service.dto.ReimbursementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reimbursement} and its DTO {@link ReimbursementDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReimbursementMapper extends EntityMapper<ReimbursementDTO, Reimbursement> {
    @Mapping(target = "advancement", source = "advancement", qualifiedByName = "id")
    ReimbursementDTO toDto(Reimbursement s);

    @Named("id")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    PaymentRequestDTO toDtoId(PaymentRequest paymentRequest);
}
