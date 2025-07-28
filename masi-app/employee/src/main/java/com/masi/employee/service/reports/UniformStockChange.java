package com.masi.employee.service.reports;


import jakarta.persistence.Column;
import lombok.Data;

import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.UUID;

//select u.code, u.name, ufd.quantity, ufd.uniform_order_stock_id, ufd.uniform_release_id FROM uniform u JOIN uniform_form_detail ufd on u.id = ufd.uniform_id
//WHERE ufd.uniform_order_stock_id is not NULL or ufd.uniform_release_id is not null
@Data
public class UniformStockChange implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 12146547567L;
    @Column(name = "id")
    private UUID id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "uniform_order_stock_id")
    private UUID uniformOrderStockId;
    @Column(name = "uniform_release_id")
    private UUID uniformReleaseId;

    @Column(name = "date")
    private ZonedDateTime date;

    public boolean isIn() {
        return uniformOrderStockId != null;
    }
    public boolean isOut() {
        return uniformReleaseId != null;
    }
}
