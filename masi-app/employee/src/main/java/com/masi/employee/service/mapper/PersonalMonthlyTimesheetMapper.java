package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.MonthlyTimeSheetReview;
import com.masi.employee.domain.PersonalMonthlyTimesheet;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.MonthlyTimeSheetReviewDTO;
import com.masi.employee.service.dto.PersonalMonthlyTimesheetDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PersonalMonthlyTimesheet} and its DTO {@link PersonalMonthlyTimesheetDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonalMonthlyTimesheetMapper extends EntityMapper<PersonalMonthlyTimesheetDTO, PersonalMonthlyTimesheet> {
    @Mapping(target = "review", source = "review", qualifiedByName = "monthlyTimeSheetReviewId")
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    PersonalMonthlyTimesheetDTO toDto(PersonalMonthlyTimesheet s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
    
    @Named("monthlyTimeSheetReviewId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MonthlyTimeSheetReviewDTO toDtoMonthlyTimeSheetReviewId(MonthlyTimeSheetReview monthlyTimeSheetReview);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
