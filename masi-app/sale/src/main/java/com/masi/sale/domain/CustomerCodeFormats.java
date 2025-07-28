package com.masi.sale.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Table("customer_code_formats")
public class CustomerCodeFormats {

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private int id;

    @Column("format_customer_code")
    private String formatCustomerCode;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("current_value")
    private int currentValue;
}
