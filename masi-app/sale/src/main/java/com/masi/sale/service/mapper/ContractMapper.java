package com.masi.sale.service.mapper;

import com.masi.sale.domain.Contract;
import com.masi.sale.service.dto.ContractDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contract} and its DTO {@link ContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {}
