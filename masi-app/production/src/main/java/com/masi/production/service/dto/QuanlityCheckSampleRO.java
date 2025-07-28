package com.masi.production.service.dto;

import com.masi.production.domain.enumeration.QcSampleStatus;
import lombok.*;

@Data
public class QuanlityCheckSampleRO {
    private QcSampleStatus status;
    private String search;
    private String company;
    private Boolean isExistItem;
    private String department;

}
