package com.masi.production.service.mapper;

import com.masi.production.domain.ManufactureOrder;
import com.masi.production.service.dto.ManufactureOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ManufactureOrder} and its DTO {@link ManufactureOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManufactureOrderMapper extends EntityMapper<ManufactureOrderDTO, ManufactureOrder> {

}
