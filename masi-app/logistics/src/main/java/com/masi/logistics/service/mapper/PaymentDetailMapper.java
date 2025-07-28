package com.masi.logistics.service.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.IncomingInvoice;
import com.masi.logistics.domain.PaymentDetail;
import com.masi.logistics.domain.PaymentRequest;
import com.masi.logistics.service.dto.IncomingInvoiceDTO;
import com.masi.logistics.service.dto.PaymentDetailDTO;
import com.masi.logistics.service.dto.PaymentRequestDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentDetail} and its DTO {@link PaymentDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentDetailMapper extends EntityMapper<PaymentDetailDTO, PaymentDetail> {
    @Mapping(target = "incomingInvoice", source = "incomingInvoice", qualifiedByName = "incomingInvoiceId")
    PaymentDetailDTO toDto(PaymentDetail s);

    @Named("incomingInvoiceId")
    @BeanMapping(ignoreByDefault = false)
    IncomingInvoiceDTO toIncomingInvoiceId(IncomingInvoice incomingInvoice);
}
