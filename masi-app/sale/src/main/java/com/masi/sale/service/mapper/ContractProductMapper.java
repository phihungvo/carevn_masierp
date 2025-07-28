package com.masi.sale.service.mapper;

import com.masi.sale.domain.ContractProduct;
import com.masi.sale.service.dto.ContractProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContractProduct} and its DTO {@link ContractProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractProductMapper extends EntityMapper<ContractProductDTO, ContractProduct> {}
