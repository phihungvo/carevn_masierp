package com.masi.employee.service.mapper;

import com.masi.employee.domain.RecruitmentRequest;
import com.masi.employee.service.dto.RecruitmentRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RecruitmentRequest} and its DTO {@link RecruitmentRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecruitmentRequestMapper extends EntityMapper<RecruitmentRequestDTO, RecruitmentRequest> {}
