package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuppliersDTO implements Serializable {

    private UUID id;

    private String code;

    private String name;

    private String email;

    private String address;

    private String phone;

    private String bankInfo;

    private String note;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updateAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deleteAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deleteBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;


    private UUID supplierGroupId;


}
