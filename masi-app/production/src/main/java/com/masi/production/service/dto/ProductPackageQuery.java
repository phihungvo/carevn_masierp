package com.masi.production.service.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import com.masi.production.domain.enumeration.ProductPackageStatus;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
@Data
public class ProductPackageQuery implements Serializable{
    @Serial
    private static final long serialVersionUID = 1222L;

    private UUID id;

    private String search;

    private Collection<UUID> workOrderIds;

    private String company;

    private String department;

    private Boolean isHasQC;

    private Collection<UUID> manufactureOrderIds;

    private Boolean isProductMaintainSpecified;

    private Collection<ProductPackageStatus> statuses;

}
