package com.masi.sale.service.mapper;

import com.masi.sale.domain.Quotation;
import com.masi.sale.service.dto.QuotationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quotation} and its DTO {@link QuotationDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuotationMapper extends EntityMapper<QuotationDTO, Quotation> {}
