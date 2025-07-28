package com.masi.sale.service.mapper;

import com.masi.sale.domain.QualityIndex;
import com.masi.sale.service.dto.QualityIndexDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QualityIndex} and its DTO {@link QualityIndexDTO}.
 */
@Mapper(componentModel = "spring")
public interface QualityIndexMapper extends EntityMapper<QualityIndexDTO, QualityIndex> {}
