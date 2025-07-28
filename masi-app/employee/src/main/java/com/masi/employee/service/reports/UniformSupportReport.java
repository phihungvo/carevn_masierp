package com.masi.employee.service.reports;

import com.masi.employee.domain.Uniform;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UniformSupportReport {
    private Uniform uniform;
    @Builder.Default
    private Integer returned = 0;
    @Builder.Default
    private Integer supported = 0;

}
