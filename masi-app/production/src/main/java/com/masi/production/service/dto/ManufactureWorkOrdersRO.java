package com.masi.production.service.dto;

import com.masi.production.domain.enumeration.ManufactureOrderType;
import com.masi.production.domain.enumeration.MoStatus;
import com.masi.production.domain.enumeration.WoStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Request object for querying manufacture orders.
 */
@Data
public class ManufactureWorkOrdersRO {
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<WoStatus> statuses;
    private String searchString;
    //toDo: remove default values
    private Collection<MoStatus> moStatuses = List.of(MoStatus.APPROVED,MoStatus.PAUSED, MoStatus.CREATED, MoStatus.NEW, MoStatus.PENDING, MoStatus.REJECTED,MoStatus.COMPLETE_PRODUCTION);
    private ManufactureOrderType manufactureOrderType;

    //constructors
    public ManufactureWorkOrdersRO() {
        //empty constructor
    }

    public ManufactureWorkOrdersRO(LocalDate fromDate, LocalDate toDate, List<WoStatus> statuses, String searchString) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.statuses = statuses;
        this.searchString = searchString;
    }


}
