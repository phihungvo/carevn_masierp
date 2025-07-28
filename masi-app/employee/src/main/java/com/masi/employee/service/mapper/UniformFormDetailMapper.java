package com.masi.employee.service.mapper;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformOrder;
import com.masi.employee.domain.UniformOrderStock;
import com.masi.employee.domain.UniformRelease;
import com.masi.employee.domain.UniformReturn;
import com.masi.employee.service.dto.*;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UniformFormDetail} and its DTO
 * {@link UniformFormDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniformFormDetailMapper extends EntityMapper<UniformFormDetailDTO, UniformFormDetail> {
    @Mapping(target = "uniform", source = "uniform", qualifiedByName = "uniformId")
    @Mapping(target = "uniformRelease", source = "uniformRelease", qualifiedByName = "uniformReleaseId")
    @Mapping(target = "uniformOrder", source = "uniformOrder", qualifiedByName = "uniformOrderId")
    @Mapping(target = "uniformReturn", source = "uniformReturn", qualifiedByName = "uniformReturnId")
    @Mapping(target = "uniformOrderStock", source = "uniformOrderStock", qualifiedByName = "uniformOrderStockId")
    UniformFormDetailDTO toDto(UniformFormDetail s);

    @Named("uniformId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    UniformDTO toDtoUniformId(Uniform uniform);

    @Named("uniformReleaseId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    UniformReleaseDTO toDtoUniformReleaseId(UniformRelease uniformRelease);

    @Named("uniformOrderId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    UniformOrderDTO toDtoUniformOrderId(UniformOrder uniformOrder);

    @Named("uniformReturnId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    UniformReturnDTO toDtoUniformReturnId(UniformReturn uniformReturn);

    @Named("uniformOrderStockId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    UniformOrderStockDTO toDtoUniformOrderStockDto(UniformOrderStock uniformOrderStock);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
