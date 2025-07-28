package com.masi.employee.service.mapper;

import com.masi.employee.domain.MonthlyTimeSheetReview;
import com.masi.employee.service.dto.MonthlyTimeSheetReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonthlyTimeSheetReview} and its DTO {@link MonthlyTimeSheetReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface MonthlyTimeSheetReviewMapper extends EntityMapper<MonthlyTimeSheetReviewDTO, MonthlyTimeSheetReview> {}
