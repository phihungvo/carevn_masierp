package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.TransactionIn;
import com.masi.logistics.service.dto.TransactionInDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransactionIn} and its DTO {@link TransactionInDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionInMapper extends EntityMapper<TransactionInDTO, TransactionIn> {}
