package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.ItemInfo;
import com.masi.logistics.service.dto.ItemInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemInfo} and its DTO {@link ItemInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemInfoMapper extends EntityMapper<ItemInfoDTO, ItemInfo> {}
