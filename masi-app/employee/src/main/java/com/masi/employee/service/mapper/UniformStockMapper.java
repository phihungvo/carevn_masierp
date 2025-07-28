package com.masi.employee.service.mapper;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformStock;
import com.masi.employee.service.dto.UniformDTO;
import com.masi.employee.service.dto.UniformStockDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UniformStock} and its DTO {@link UniformStockDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniformStockMapper extends EntityMapper<UniformStockDTO, UniformStock> {
    @Mapping(target = "uniform", source = "uniform", qualifiedByName = "uniformId")
    UniformStockDTO toDto(UniformStock s);

    @Named("uniformId")
    @BeanMapping(ignoreByDefault = false)
//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
    UniformDTO toDtoUniformId(Uniform uniform);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
