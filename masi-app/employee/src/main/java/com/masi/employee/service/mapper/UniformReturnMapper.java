package com.masi.employee.service.mapper;

import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformReturn;
import com.masi.employee.service.dto.UniformFormDetailDTO;
import com.masi.employee.service.dto.UniformFormDetailRawDTO;
import com.masi.employee.service.dto.UniformReturnDTO;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link UniformReturn} and its DTO {@link UniformReturnDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniformReturnMapper extends EntityMapper<UniformReturnDTO, UniformReturn> {

    @Mapping(target = "returnDetails", ignore = true)
    @Mapping(source = "uniformFormDetails", target = "uniformFormDetail", qualifiedByName = "uniformFormDetailRawMapping")
    UniformReturnDTO toDto(UniformReturn uniformReturn);

    @Named("uniformFormDetailRawMapping")
    default Set<UniformFormDetailRawDTO> map(Set<UniformFormDetail> value) {
        if (value == null) {
            return new HashSet<UniformFormDetailRawDTO>();
        }
        return value.stream()
            .map(this::toUniformFormDetailDTO)
            .collect(Collectors.toSet());
    }

    @Named("uniformFormDetails1")
    default Set<UniformFormDetailDTO> map1(Set<UniformFormDetail> value) {
        if (value == null) {
            return new HashSet<UniformFormDetailDTO>();
        }
        return value.stream()
            .map(this::toUniformFormDetailDTO1)
            .collect(Collectors.toSet());
    }

    UniformFormDetailRawDTO toUniformFormDetailDTO(UniformFormDetail uniformFormDetail);

    UniformFormDetailDTO toUniformFormDetailDTO1(UniformFormDetail uniformFormDetail);
}
