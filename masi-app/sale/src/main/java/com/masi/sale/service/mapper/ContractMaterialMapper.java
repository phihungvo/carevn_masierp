package com.masi.sale.service.mapper;

import com.masi.sale.domain.ContractMaterial;
import com.masi.sale.service.dto.ContractMaterialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContractMaterial} and its DTO {@link ContractMaterialDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractMaterialMapper extends EntityMapper<ContractMaterialDTO, ContractMaterial> {}
