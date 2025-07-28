package com.masi.employee.service.reports;

import com.masi.employee.domain.Uniform;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UniformReport {
    private Uniform uniform;
    @Builder.Default
    private Integer quantity=0;
    private String status;
}
