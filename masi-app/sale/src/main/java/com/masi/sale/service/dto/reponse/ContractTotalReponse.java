package com.masi.sale.service.dto.reponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ContractTotalReponse {
    private Long total_quantity;
    private BigDecimal total_value;
}
