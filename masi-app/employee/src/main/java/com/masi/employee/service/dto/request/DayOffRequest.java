package com.masi.employee.service.dto.request;

import com.masi.employee.domain.enumeration.WorkPlace;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DayOffRequest {
    private UUID idEmployee;
    private String CompanyId;
    private WorkPlace workPlace;

}
