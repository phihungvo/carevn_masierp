package com.masi.employee.service.mapper;

import com.masi.employee.domain.Workspace;
import com.masi.employee.service.dto.WorkspaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Workspace} and its DTO {@link WorkspaceDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkspaceMapper extends EntityMapper<WorkspaceDTO, Workspace> {}
