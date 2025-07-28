package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.masi.logistics.domain.Suppliers} entity. This class is used
 * in {@link com.masi.logistics.web.rest.SuppliersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 */
@Data
public class SuppliersCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter email;

    private StringFilter address;

    private StringFilter phone;

    private StringFilter note;

    private ZonedDateTimeFilter createAt;

    private StringFilter createBy;

    private ZonedDateTimeFilter updateAt;

    private StringFilter updateBy;

    private ZonedDateTimeFilter deleteAt;

    private StringFilter deleteBy;

    private StringFilter company;

    private UUIDFilter supplierGroupId;

    public SuppliersCriteria() {}

    // Getters and setters for each field

}
