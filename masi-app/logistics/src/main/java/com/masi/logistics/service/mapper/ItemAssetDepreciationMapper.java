package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.ItemAssetDepreciation;
import com.masi.logistics.service.dto.ItemAssetDepreciationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemAssetDepreciation} and its DTO {@link ItemAssetDepreciationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemAssetDepreciationMapper extends EntityMapper<ItemAssetDepreciationDTO, ItemAssetDepreciation> {}
