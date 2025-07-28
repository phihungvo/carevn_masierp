package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Uom;
import com.masi.logistics.domain.UomGroup;
import com.masi.logistics.service.dto.UomDTO;
import com.masi.logistics.service.dto.UomGroupDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UomGroup} and its DTO {@link UomGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface UomGroupMapper extends EntityMapper<UomGroupDTO, UomGroup> {
    @Mapping(target = "baseUom", source = "baseUom", qualifiedByName = "uomId")
    UomGroupDTO toDto(UomGroup s);

    @Named("uomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UomDTO toDtoUomId(Uom uom);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
