package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformOrder;
import com.masi.employee.domain.UniformOrderStock;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.UniformFormDetailDTO;
import com.masi.employee.service.dto.UniformOrderDTO;
import com.masi.employee.service.dto.UniformOrderStockDTO;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link UniformOrderStock} and its DTO {@link UniformOrderStockDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniformOrderStockMapper extends EntityMapper<UniformOrderStockDTO, UniformOrderStock> {

    @Mapping(target = "uniformFormDetailDTO", ignore = true)
    @Mapping(target = "uniformFormDetail", source = "uniformFormDetail")
    @Mapping(target = "createdByProfile", source = "createdByProfile", qualifiedByName = "employeeToEmployeeDTO")
    @Mapping(target = "uniformOrderDTO", source = "uniformOrder", qualifiedByName = "uniformOrderToUniformOrderDTO")
    UniformOrderStockDTO toDto(UniformOrderStock entity);

    @Mapping(target = "persisted", ignore = true)
    @Mapping(target = "uniformFormDetail", source = "uniformFormDetail")
    UniformOrderStock toEntity(UniformOrderStockDTO dto);

    @Named("employeeToEmployeeDTO")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    EmployeeDTO toEmployeeDto(Employee entity);

    @Named("uniformOrderToUniformOrderDTO")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "code", source = "code")
    UniformOrderDTO toUniformOrderDto(UniformOrder entity);

    @IterableMapping(elementTargetType = UniformFormDetailDTO.class)
    Set<UniformFormDetailDTO> toDto(Set<UniformFormDetail> entityList);

    @IterableMapping(elementTargetType = UniformFormDetail.class)
    Set<UniformFormDetail> toEntity(Set<UniformFormDetailDTO> dtoList);
}
