package com.carevn.masi.service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanyQuery {

    private String search;

    private UUID parentId;

    private Boolean isParentIdSpecified;
}
