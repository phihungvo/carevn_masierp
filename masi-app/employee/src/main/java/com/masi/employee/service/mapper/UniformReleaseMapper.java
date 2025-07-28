package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformRelease;
import com.masi.employee.service.dto.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UniformRelease} and its DTO
 * {@link UniformReleaseDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniformReleaseMapper extends EntityMapper<UniformReleaseDTO, UniformRelease> {
    // config mapper to map from entity to dto include hashSet
    @Mapping(target = "uniformFormDetails", source = "uniformFormDetails", qualifiedByName = "uniformFormDetails1")
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employee")
    UniformReleaseDTO toDto(UniformRelease s);

    @Named("employee")
    EmployeeDTO toEmployeeDTO(Employee employee);

    @Named("uniformFormDetails2")
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
