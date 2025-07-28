package com.masi.production.service.dto;

import com.masi.production.domain.enumeration.QcSampleStatus;

@org.springdoc.core.annotations.ParameterObject
public class QuantityCheckQuery {

    private QcSampleStatus status;
    private String search;

    public QcSampleStatus getStatus() {
        return status;
    }

    public void setStatus(QcSampleStatus status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
