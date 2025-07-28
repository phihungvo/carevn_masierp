package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.InterviewSchedule;
import com.masi.employee.domain.RecruitmentRequest;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.InterviewScheduleDTO;
import com.masi.employee.service.dto.RecruitmentRequestDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InterviewSchedule} and its DTO {@link InterviewScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface InterviewScheduleMapper extends EntityMapper<InterviewScheduleDTO, InterviewSchedule> {
    @Mapping(target = "recruitmentRequest", source = "recruitmentRequest", qualifiedByName = "recruitmentRequestId")
    @Mapping(target = "interviewer", source = "interviewer", qualifiedByName = "interviewerId")
    InterviewScheduleDTO toDto(InterviewSchedule s);

    @Named("recruitmentRequestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "jobTitle", source = "jobTitle")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "status", source = "status")

    RecruitmentRequestDTO toDtoRecruitmentRequestId(RecruitmentRequest recruitmentRequest);

    @Named("interviewerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    EmployeeDTO toDtoInterviewerId(Employee interviewer);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
