package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.TransactionOut;
import com.masi.logistics.service.dto.TransactionOutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransactionOut} and its DTO {@link TransactionOutDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionOutMapper extends EntityMapper<TransactionOutDTO, TransactionOut> {}
