package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Currency;
import com.masi.logistics.service.dto.CurrencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Currency} and its DTO {@link CurrencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper extends EntityMapper<CurrencyDTO, Currency> {}
