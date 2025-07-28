package com.masi.production.service.dto;

import com.masi.production.domain.enumeration.QcSampleStatus;
import lombok.*;

@Data
@org.springdoc.core.annotations.ParameterObject
public class ProductRoutingQuery {
    private String search;
    private String factoryId;
    private String storageId;
    private String company;
    private String department;


}


