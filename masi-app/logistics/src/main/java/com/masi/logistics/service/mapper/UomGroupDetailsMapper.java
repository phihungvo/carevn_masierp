package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Uom;
import com.masi.logistics.domain.UomGroup;
import com.masi.logistics.domain.UomGroupDetails;
import com.masi.logistics.service.dto.UomDTO;
import com.masi.logistics.service.dto.UomGroupDTO;
import com.masi.logistics.service.dto.UomGroupDetailsDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UomGroupDetails} and its DTO {@link UomGroupDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UomGroupDetailsMapper extends EntityMapper<UomGroupDetailsDTO, UomGroupDetails> {
    @Mapping(target = "baseUom", source = "baseUom", qualifiedByName = "uomId")
    @Mapping(target = "altUom", source = "altUom", qualifiedByName = "uomId")
    @Mapping(target = "uomGroup", source = "uomGroup", qualifiedByName = "uomGroupId")
    UomGroupDetailsDTO toDto(UomGroupDetails s);

    @Named("uomId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    UomDTO toDtoUomId(Uom uom);

    @Named("uomGroupId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    UomGroupDTO toDtoUomGroupId(UomGroup uomGroup);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
