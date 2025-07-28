package com.masi.employee.service.reports;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class UniformStockAggregate implements Serializable {
    @Serial
    private static final long serialVersionUID = 12121235723L;
    @Column(name = "id")
    private UUID id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;

    @Column(name = "stock")
    private int stock = 0;

    @Column(name = "base_price")
    private float basePrice;

    @Column(name = "unit_id")
    private UUID unitId;

    private String unitName="";

    public float getValuation() {
        return stock * basePrice;
    }

    public UniformStockAggregate rollback(Collection<UniformStockChange> change) {
        for (UniformStockChange c : change) {
            if (c.isIn()) {
                stock -= c.getQuantity();
            } else {
                stock += c.getQuantity();
            }
        }
        return this;
    }

    public UniformStockAggregate apply(Collection<UniformStockChange> change) {
        for (UniformStockChange c : change) {
            if (c.isIn()) {
                stock += c.getQuantity();
            } else {
                stock -= c.getQuantity();
            }
        }
        return this;
    }
}
