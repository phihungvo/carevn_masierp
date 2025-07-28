package com.masi.employee.service.mapper;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformOrder;
import com.masi.employee.domain.UniformRelease;
import com.masi.employee.domain.UniformReturn;
import com.masi.employee.service.dto.UniformDTO;
import com.masi.employee.service.dto.UniformFormDetailDTO;
import com.masi.employee.service.dto.UniformFormDetailRawDTO;
import com.masi.employee.service.dto.UniformOrderDTO;
import com.masi.employee.service.dto.UniformReleaseDTO;
import com.masi.employee.service.dto.UniformReturnDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UniformFormDetail} and its DTO
 * {@link UniformFormDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniformFormDetailRawMapper extends EntityMapper<UniformFormDetailRawDTO, UniformFormDetail> {

    @Mapping(target = "uniform", source = "uniform", qualifiedByName = "uniformId")
    UniformFormDetailRawDTO toDto(UniformFormDetail s);

    @Named("uniformId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UniformDTO toDtoUniformId(Uniform uniform);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
