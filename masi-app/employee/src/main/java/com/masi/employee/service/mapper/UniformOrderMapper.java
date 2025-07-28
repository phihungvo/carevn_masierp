package com.masi.employee.service.mapper;

import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformOrder;
import com.masi.employee.domain.UniformOrderProcess;
import com.masi.employee.service.dto.UniformFormDetailDTO;
import com.masi.employee.service.dto.UniformOrderDTO;
import com.masi.employee.service.dto.UniformOrderProcessDTO;

import org.mapstruct.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link UniformOrder} and its DTO {@link UniformOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniformOrderMapper extends EntityMapper<UniformOrderDTO, UniformOrder> {
    @Mapping(target = "uniformFormDetails", source = "uniformFormDetails", qualifiedByName = "uniformOrderDetails")
    @Mapping(target = "uniformOrderProcesses", source = "uniformOrderProcesses", qualifiedByName = "uniformOrderProcess")
    UniformOrderDTO toDto(UniformOrder s);

    @Named("uniformOrderDetails")
    default List<UniformFormDetailDTO> map(Collection<UniformFormDetail> value) {
        if (value == null) {
            return new ArrayList<>();
        }
        return value.stream()
                .map(this::toUniformFormDetailDTO)
                .collect(Collectors.toList());
    }

    @Named("uniformOrderProcess")
    default List<UniformOrderProcessDTO> mapUniformOrderProcess(Collection<UniformOrderProcess> value) {
        if (value == null) {
            return new ArrayList<>();
        }
        return value.stream()
                .map(this::toProcessDto)
                .collect(Collectors.toList());
    }

    @Mapping(source = "uniformOrder", ignore = true, target = "uniformOrder")
    UniformFormDetailDTO toUniformFormDetailDTO(UniformFormDetail detail);

    @Mapping(source = "uniformOrder", ignore = true, target = "uniformOrder")
    UniformOrderProcessDTO toProcessDto(UniformOrderProcess uniformOrderProcess);


}
