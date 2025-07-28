package com.masi.employee.service.reports;

import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@ParameterObject
@Data
public class ReportQuery {
    private  LocalDate fromDate=LocalDate.of(2020,1,1);
    private LocalDate toDate=LocalDate.of(2100,12,31);
    private String company;
}
