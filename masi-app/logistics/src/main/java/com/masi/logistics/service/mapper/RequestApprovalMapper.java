package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.RequestApproval;
import com.masi.logistics.service.dto.RequestApprovalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RequestApproval} and its DTO {@link RequestApprovalDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestApprovalMapper extends EntityMapper<RequestApprovalDTO, RequestApproval> {}
