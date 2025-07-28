package com.masi.employee.service.mapper;

import com.masi.employee.domain.Documentary;
import com.masi.employee.service.dto.DocumentaryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Documentary} and its DTO {@link DocumentaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentaryMapper extends EntityMapper<DocumentaryDTO, Documentary> {}
