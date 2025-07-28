package com.masi.employee.service.dto.reponse;

import com.masi.employee.service.dto.AnnualLeaveDTO;

public class AnnualLeaveDTOReponse {
    private AnnualLeaveDTO annualLeave_FACTORY;
    private AnnualLeaveDTO annualLeave_OFFICE;


    public AnnualLeaveDTO getAnnualLeave_FACTORY() {
        return annualLeave_FACTORY;
    }

    public void setAnnualLeave_FACTORY(AnnualLeaveDTO annualLeave_FACTORY) {
        this.annualLeave_FACTORY = annualLeave_FACTORY;
    }

    public AnnualLeaveDTO getAnnualLeave_OFFICE() {
        return annualLeave_OFFICE;
    }

    public void setAnnualLeave_OFFICE(AnnualLeaveDTO annualLeave_OFFICE) {
        this.annualLeave_OFFICE = annualLeave_OFFICE;
    }
}
