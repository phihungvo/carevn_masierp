package com.masi.employee.service.reports;

import com.masi.employee.domain.enumeration.Position;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class RecruitmentQuantityQuery {
    @Column("position")
    private String position;

    @Column("total_request")
    private Integer totalRequest;
    public String getPosition() {
        try {
            return Position.valueOf(position).toVietnamese();
        } catch (Exception e) {
            return position;
        }
    }
}
