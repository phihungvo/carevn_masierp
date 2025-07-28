package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.InventoriesCheckDetail;
import com.masi.logistics.service.dto.InventoriesCheckDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventoriesCheckDetail} and its DTO {@link InventoriesCheckDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoriesCheckDetailMapper extends EntityMapper<InventoriesCheckDetailDTO, InventoriesCheckDetail> {}
