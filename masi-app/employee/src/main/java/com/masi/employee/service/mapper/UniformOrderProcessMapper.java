package com.masi.employee.service.mapper;

import com.masi.employee.domain.UniformOrder;
import com.masi.employee.domain.UniformOrderProcess;
import com.masi.employee.service.dto.UniformOrderDTO;
import com.masi.employee.service.dto.UniformOrderProcessDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UniformOrderProcess} and its DTO
 * {@link UniformOrderProcessDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniformOrderProcessMapper extends EntityMapper<UniformOrderProcessDTO, UniformOrderProcess> {
    @Mapping(target = "uniformOrder", source = "uniformOrder", qualifiedByName = "uniformOrderId")
    UniformOrderProcessDTO toDto(UniformOrderProcess s);

    @Named("uniformOrderId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    UniformOrderDTO toDtoUniformOrderId(UniformOrder uniformOrder);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
