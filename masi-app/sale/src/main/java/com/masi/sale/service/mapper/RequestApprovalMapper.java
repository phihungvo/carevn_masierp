package com.masi.sale.service.mapper;

import com.masi.sale.domain.RequestApproval;
import com.masi.sale.service.dto.RequestApprovalDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link RequestApproval} and its DTO {@link RequestApprovalDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestApprovalMapper extends EntityMapper<RequestApprovalDTO, RequestApproval> {}
