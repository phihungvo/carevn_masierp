package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.VatRate;
import com.masi.logistics.service.dto.VatRateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VatRate} and its DTO {@link VatRateDTO}.
 */
@Mapper(componentModel = "spring")
public interface VatRateMapper extends EntityMapper<VatRateDTO, VatRate> {}
