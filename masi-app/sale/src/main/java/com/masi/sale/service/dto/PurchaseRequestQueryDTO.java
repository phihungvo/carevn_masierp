package com.masi.sale.service.dto;

import com.masi.sale.domain.enumeration.PurchaseRequestStatus;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

@Data
@ParameterObject
public class PurchaseRequestQueryDTO implements Serializable{
    private String searchString;
    private Collection<PurchaseRequestStatus> statuses;
    private LocalDate createdFrom;
    private LocalDate createdTo;
}
