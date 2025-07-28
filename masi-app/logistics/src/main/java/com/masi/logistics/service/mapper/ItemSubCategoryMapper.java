package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.ItemSubCategory;
import com.masi.logistics.service.dto.ItemSubCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemSubCategory} and its DTO {@link ItemSubCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemSubCategoryMapper extends EntityMapper<ItemSubCategoryDTO, ItemSubCategory> {}
