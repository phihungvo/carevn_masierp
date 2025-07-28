package com.masi.employee.service.mapper;

import com.masi.employee.domain.RecruitmentReviewRequest;
import com.masi.employee.service.dto.RecruitmentReviewRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RecruitmentReviewRequest} and its DTO {@link RecruitmentReviewRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecruitmentReviewRequestMapper extends EntityMapper<RecruitmentReviewRequestDTO, RecruitmentReviewRequest> {}
