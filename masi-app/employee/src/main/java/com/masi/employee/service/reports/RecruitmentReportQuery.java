package com.masi.employee.service.reports;

import com.masi.employee.domain.enumeration.InterviewResult;
import com.masi.employee.domain.enumeration.Position;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class RecruitmentReportQuery {
    @Column("interview_result")
    private InterviewResult result;

    @Column("position")
    private String position;


    public String getPosition() {
        try {
            return Position.valueOf(position).toVietnamese();
        } catch (Exception e) {
            return position;
        }
    }
}
