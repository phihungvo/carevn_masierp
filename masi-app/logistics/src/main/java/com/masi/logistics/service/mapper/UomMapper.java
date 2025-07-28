package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Uom;
import com.masi.logistics.service.dto.UomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Uom} and its DTO {@link UomDTO}.
 */
@Mapper(componentModel = "spring")
public interface UomMapper extends EntityMapper<UomDTO, Uom> {}
