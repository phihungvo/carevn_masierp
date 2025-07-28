package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.ItemInfoDetail;
import com.masi.logistics.service.dto.ItemInfoDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemInfoDetail} and its DTO {@link ItemInfoDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemInfoDetailMapper extends EntityMapper<ItemInfoDetailDTO, ItemInfoDetail> {}
