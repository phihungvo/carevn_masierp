package com.masi.sale.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

import com.masi.sale.domain.enumeration.OrderStatus;

import lombok.ToString;

@Setter
@Getter
@ParameterObject
@ToString
public class OrderQueryDTO implements  Serializable{

    private Collection<OrderStatus> statuses ;

    private String searchString;
    private String company;

    private UUID customerId;

    private LocalDate startDate;
    private LocalDate endDate;

}
