package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.domain.LeaveRequestReview;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.LeaveRequestDTO;
import com.masi.employee.service.dto.LeaveRequestReviewDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaveRequestReview} and its DTO {@link LeaveRequestReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeaveRequestReviewMapper extends EntityMapper<LeaveRequestReviewDTO, LeaveRequestReview> {
    @Mapping(target = "leaveRequest", source = "leaveRequest", qualifiedByName = "leaveRequestId")
    @Mapping(target = "reviewer", source = "reviewer", qualifiedByName = "employeeId")
    LeaveRequestReviewDTO toDto(LeaveRequestReview s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);

    @Named("leaveRequestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LeaveRequestDTO toDtoLeaveRequestId(LeaveRequest leaveRequest);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
