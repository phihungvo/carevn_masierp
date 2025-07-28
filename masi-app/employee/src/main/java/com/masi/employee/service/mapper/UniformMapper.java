package com.masi.employee.service.mapper;

import com.masi.employee.domain.Uniform;
import com.masi.employee.service.dto.UniformDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Uniform} and its DTO {@link UniformDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniformMapper extends EntityMapper<UniformDTO, Uniform> {}
