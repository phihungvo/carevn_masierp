package com.masi.sale.service.mapper;

import com.masi.sale.domain.TemplateContract;
import com.masi.sale.service.dto.TemplateContractDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TemplateContract} and its DTO {@link TemplateContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface TemplateContractMapper extends EntityMapper<TemplateContractDTO, TemplateContract> {}
