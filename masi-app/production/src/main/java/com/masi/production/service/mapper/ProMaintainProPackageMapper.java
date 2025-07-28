package com.masi.production.service.mapper;

import com.masi.production.domain.ProMaintainProPackage;
import com.masi.production.service.dto.ProMaintainProPackageDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ProMaintainProPackage} and its DTO {@link ProMaintainProPackageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProMaintainProPackageMapper extends EntityMapper<ProMaintainProPackageDTO, ProMaintainProPackage> {}
