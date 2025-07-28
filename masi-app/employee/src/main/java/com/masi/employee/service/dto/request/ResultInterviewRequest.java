package com.masi.employee.service.dto.request;

import com.masi.employee.domain.enumeration.InterviewResult;

public class ResultInterviewRequest {
    private InterviewResult interviewResult;
    private String rate;

    public InterviewResult getInterviewResult() {
        return interviewResult;
    }

    public void setInterviewResult(InterviewResult interviewResult) {
        this.interviewResult = interviewResult;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
