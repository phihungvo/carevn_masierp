package com.masi.sale.service.mapper;

import com.masi.sale.domain.Contract;
import com.masi.sale.domain.ContractFile;
import com.masi.sale.service.dto.ContractDTO;
import com.masi.sale.service.dto.ContractFileDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContractFile} and its DTO {@link ContractFileDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractFileMapper extends EntityMapper<ContractFileDTO, ContractFile> {
    @Mapping(target = "contract", source = "contract", qualifiedByName = "contractId")
    ContractFileDTO toDto(ContractFile s);

    @Named("contractId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContractDTO toDtoContractId(Contract contract);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
