package com.masi.production.service.mapper;

import com.masi.production.domain.Storage;
import com.masi.production.service.dto.StorageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Storage} and its DTO {@link StorageDTO}.
 */
@Mapper(componentModel = "spring")
public interface StorageMapper extends EntityMapper<StorageDTO, Storage> {}
