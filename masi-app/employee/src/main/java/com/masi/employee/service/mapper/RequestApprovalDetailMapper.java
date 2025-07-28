package com.masi.employee.service.mapper;

import com.masi.employee.domain.RequestApprovalDetail;
import com.masi.employee.service.dto.RequestApprovalDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RequestApprovalDetail} and its DTO {@link RequestApprovalDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestApprovalDetailMapper extends EntityMapper<RequestApprovalDetailDTO, RequestApprovalDetail> {}
