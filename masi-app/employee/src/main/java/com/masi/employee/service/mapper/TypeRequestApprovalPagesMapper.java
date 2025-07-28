package com.masi.employee.service.mapper;

import com.masi.employee.domain.TypeRequestApprovalPages;
import com.masi.employee.service.dto.TypeRequestApprovalPagesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeRequestApprovalPages} and its DTO {@link TypeRequestApprovalPagesDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeRequestApprovalPagesMapper extends EntityMapper<TypeRequestApprovalPagesDTO, TypeRequestApprovalPages> {}
