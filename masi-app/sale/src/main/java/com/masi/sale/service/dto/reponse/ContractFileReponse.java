package com.masi.sale.service.dto.reponse;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContractFileReponse {
    private String nameContractName;
    private String nameAppendixName;

    public ContractFileReponse(String nameContractName, String nameAppendixName) {
        this.nameContractName = nameContractName;
        this.nameAppendixName = nameAppendixName;
    }

}
