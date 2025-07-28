package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.ItemLiquidation;
import com.masi.logistics.service.dto.ItemLiquidationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemLiquidation} and its DTO {@link ItemLiquidationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemLiquidationMapper extends EntityMapper<ItemLiquidationDTO, ItemLiquidation> {}
