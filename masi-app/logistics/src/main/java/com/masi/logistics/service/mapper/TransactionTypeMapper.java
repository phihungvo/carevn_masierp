package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.TransactionType;
import com.masi.logistics.service.dto.TransactionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransactionType} and its DTO {@link TransactionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionTypeMapper extends EntityMapper<TransactionTypeDTO, TransactionType> {}
