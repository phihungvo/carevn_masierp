package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class ContractFile {
//    private String contractFile;
//
//    private String appendixFile;

    List<String> contractFileNew;

}
