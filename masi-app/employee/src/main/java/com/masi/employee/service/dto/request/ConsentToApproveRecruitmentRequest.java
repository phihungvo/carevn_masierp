package com.masi.employee.service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class ConsentToApproveRecruitmentRequest {
    @NotNull(message = "must not be null")
    public String approvalSignFile;

}
