package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.IncomingInvoice;
import com.masi.logistics.domain.RelatedCosts;
import com.masi.logistics.service.dto.IncomingInvoiceDTO;
import com.masi.logistics.service.dto.RelatedCostsDTO;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper for the entity {@link RelatedCosts} and its DTO {@link RelatedCostsDTO}.
 */
@Mapper(componentModel = "spring")
public interface RelatedCostsMapper extends EntityMapper<RelatedCostsDTO, RelatedCosts> {

    @Mapping(source = "invoice", target = "invoice", qualifiedByName = "toInvoiceId")
    RelatedCostsDTO toDto(RelatedCosts relatedCosts);

    @Named("toInvoiceId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(source = "suppliers", target = "suppliers")
    IncomingInvoiceDTO toIncomingInvoiceDTO(IncomingInvoice incomingInvoice);
}
