package com.masi.sale.service.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiResponseContract<T> {
    private List<T> data;
    private ContractTotalReponse totalRecord;
}
