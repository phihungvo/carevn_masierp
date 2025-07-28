package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.ExplanationReview;
import com.masi.employee.domain.TimeKeepingExplanation;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.ExplanationReviewDTO;
import com.masi.employee.service.dto.TimeKeepingExplanationDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExplanationReview} and its DTO {@link ExplanationReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExplanationReviewMapper extends EntityMapper<ExplanationReviewDTO, ExplanationReview> {
    @Mapping(target = "reviewer", source = "reviewer", qualifiedByName = "employeeId")
    @Mapping(target = "explanation", source = "explanation", qualifiedByName = "timeKeepingExplanationId")
    ExplanationReviewDTO toDto(ExplanationReview s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);

    @Named("timeKeepingExplanationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TimeKeepingExplanationDTO toDtoTimeKeepingExplanationId(TimeKeepingExplanation timeKeepingExplanation);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
