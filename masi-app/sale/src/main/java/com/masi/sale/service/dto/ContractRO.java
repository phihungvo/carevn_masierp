package com.masi.sale.service.dto;

import com.masi.sale.domain.enumeration.ContractStatus;
import com.masi.sale.domain.enumeration.ContractType;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ContractRO {
    private String search;
    private ContractType contractType;
    private List<ContractStatus> contractStatusList;
    private String proteinPercent;
    private LocalDate contractValidFrom;
    private LocalDate contractValidTo;
    private String company;
    private String department;
    private Boolean isExpired;
    private Boolean withFull = true;
    private List<UUID> employeeOwner;
    private List<UUID> companyName;
    private Boolean withSum=true;
    private Boolean withReview =false;

    public List<ContractStatus> getContractStatusList() {
        if (contractStatusList == null) {
            contractStatusList = new ArrayList<>();
        }
        return contractStatusList;
    }

    public void addContractStatus(ContractStatus status) {
        getContractStatusList().add(status);
    }
}
